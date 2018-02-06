#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <dirent.h>
#include <arpa/inet.h>//include IPPOTO_TCP, struct sockaddr_in,
#include <sys/socket.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/time.h>
#include <sys/wait.h>
#include <sys/resource.h>
#include <sys/ptrace.h>
#include <sys/signal.h>
#include <sys/user.h> /* for struct user_regs_struct */

#include "syscalls.h"//syscall list

//使用数值表示语言, 便于比较, 可以任意数值, 只要每个值不同即可
#define LANG_C       20
#define LANG_CPP     21
#define LANG_JAVA    22
#define LANG_PYTHON2 23
#define LANG_PYTHON3 24


#define BUF_SIZE  4096
#define FILE_PATH_SIZE 256
#define LINE_BUF_SIZE 1024

//一下常量作为result_list数组的下标
#define QUEUING 0
#define COMPILING 1
#define RUNNING 2
#define ACCEPTED 3
#define PRESENTATION_ERROR 4
#define WRONG_ANSWER 5
#define TIME_LIMIT_EXCEEDED 6
#define MEMORY_LIMIT_EXCEEDED 7
#define OUTPUT_LIMIT_EXCEEDED 8
#define RUNTIME_ERROR 9
#define SYSTEM_ERROR 10
#define COMPILATION_ERROR 11



const char * result_list[] = {
/*index   value*/
  /*0*/   "Queuing",
  /*1*/   "Compiling",
  /*2*/   "Running",
  /*3*/   "Accepted",
  /*4*/   "Presentation Error",
  /*5*/   "Wrong Answer",
  /*6*/   "Time Limit Exceeded",
  /*7*/   "Memory Limit Exceeded",
  /*8*/   "Output Limit Exceeded",
  /*9*/   "Runtime Error",
  /*10*/  "System Error",
  /*11*/  "Compilation Error"
};


//一下几个变量保存系统配置, 其默认值在读取配置文件后可能会被修改
int DEBUG = 0;
int port = 2345;
int output_file_size = 4096;
char restrict_syscalls[100][25] = {0};
char oj_home[256];





/*
web服务器提交新的代码请求时发送如下格式的json
{
    "submitID": 1,
    "language":"c++",
    "timeLimit":"1000",
    "memLimit":"65535",
    "runningFolder":"/a/s/",
    "testPointDataFolder":"/g/s"
}
*/
struct Submit
{
    int  submitID;
    int  language;  //should map str to int value
    int  timeLimit; //should convert mirco second from json to second
    int  memLimit;
    char runningFolder[FILE_PATH_SIZE];
    char testPointDataFolder[FILE_PATH_SIZE];
};



//评测状态发生改变时候, 相应如下格式json给web server
/*
{
    "submitID":1,
    "state":"System Error"
}
*/
struct Judge_state
{
    int submitID;
    char state[32];
};

void get_judge_state_json(struct Submit submit, char * json, int index)
{
    sprintf(json, "{\"submitID\":%d,\"state\":\"%s\"}", submit.submitID, result_list[index]);
}



//{"testPointID":1,"timeConsume":234,"memConsume":3421,result":"accepted"},
struct Test_point_detail
{
    int  testPointID;
    int  timeConsume;
    int  memConsume;
    int  returnVal;
    int  result;
};


/*
响应给web服务器的评测结果
{
    "submitID":1,
    "errorMessage":"msg",
    "compileResult":1,
    "compilerOutput":"compile error",
    "judgeDetail":
    [
        {"testPointID":1,"timeConsume":234,"memConsume":3421,"returnVal":0,"result":"accepted"},
        {"testPointID":1,"timeConsume":234,"memConsume":3421,"returnVal":0,"result":"accepted"},
        {"testPointID":1,"timeConsume":234,"memConsume":3421,"returnVal":0,"result":"accepted"},
        {"testPointID":1,"timeConsume":234,"memConsume":3421,"returnVal":0,"result":"accepted"}
    ]
}
*/

struct Result
{
    int  submitID;
    char errorMessage[128];
    int  compileResult;
    char compilerOutput[4 * 1024];
    int  testPointCount;
    struct Test_point_detail detail[30];
};



void print_submit(struct Submit submit)
{
    puts("new submit:");
    printf("           submitID: %d\n", submit.submitID);
    printf("           language: %d\n", submit.language);
    printf("          timeLimit: %dms\n", submit.timeLimit);
    printf("           memLimit: %dKB\n", submit.memLimit);
    printf("      runningFolder: %s\n", submit.runningFolder);
    printf("testPointDataFolder: %s\n", submit.testPointDataFolder);
}



void print_judge_result(struct Result judge_result)
{
    puts("judge result:");
    printf("      submitID: %d\n", judge_result.submitID);
    printf("  errorMessage: %s\n", judge_result.errorMessage);
    printf(" compileResult: %d\n", judge_result.compileResult);
    printf("compilerOutput: %s\n", judge_result.compilerOutput);
    for (int i = 0; i < judge_result.testPointCount; i++)
        printf("  testPointID: %d, time: %d, mem: %d, result: %s\n", judge_result.detail[i].testPointID, judge_result.detail[i].timeConsume, judge_result.detail[i].memConsume, result_list[judge_result.detail[i].result]);
}



void get_result_json(char *json, const struct Result result)
{
    const char *jsonPattern   =    "{\"submitID\":%d,\"errorMessage\":\"%s\",\"compileResult\":%d,\"compilerOutput\":\"%s\",\"judgeDetail\":[%s]}";
    const char *resultPattern =    "{\"testPointID\":%d,\"timeConsume\":%d,\"memConsume\":%d,\"result\":\"%s\"},";
    const char *endResultPattern = "{\"testPointID\":%d,\"timeConsume\":%d,\"memConsume\":%d,\"result\":\"%s\"}";

    char results[1024] = "";
    for (int i = 0; i < result.testPointCount; i++)
    {
        char buf[128] = "";
        if (i < result.testPointCount - 1)
        {
            sprintf(buf,    resultPattern, result.detail[i].testPointID, result.detail[i].timeConsume, result.detail[i].memConsume, result_list[result.detail[i].result]);
            strcat(results, buf);
        }
        else
        {
            sprintf(buf, endResultPattern, result.detail[i].testPointID, result.detail[i].timeConsume, result.detail[i].memConsume, result_list[result.detail[i].result]);
            strcat(results, buf);
        }
    }
    sprintf(json, jsonPattern, result.submitID, result.errorMessage, result.compileResult, result.compilerOutput, results);
}



struct Result get_system_error_result(struct Submit submit, const char *errorMessage)
{
    struct Result result;
    result.submitID = submit.submitID;
    strcmp(result.errorMessage, errorMessage);
    result.compileResult = 0;
    strcpy(result.compilerOutput, "");
    result.testPointCount = 0;
    return result;
}



void format_string_for_json(char *src, char *desc)
{
    int len = strlen(src), i = 0, j = 0;
    for (; i < len; i++)
    {
        if (src[i] == '\"')
        {
            desc[j++] = '\\';//printf("d[%d]: '%c' ", j - 1, desc[j - 1]);
            desc[j++] = '\"';//printf("d[%d]: '%c'\n", j - 1, desc[j - 1]);
        }
        else if (src[i] == '\n')
        {
            desc[j++] = '\\';//printf("d[%d]: '%c' ", j - 1, desc[j - 1]);
            desc[j++] = 'n';//printf("d[%d]: '%c'\n", j - 1, desc[j - 1]);
        }
        else if (src[i] == '\r')
        {
            desc[j++] = '\\';//printf("d[%d]: '%c' ", j - 1, desc[j - 1]);
            desc[j++] = 'r';//printf("d[%d]: '%c'\n", j - 1, desc[j - 1]);
        }
        else if (src[i] == '\\')
        {
            desc[j++] = '\\';//printf("d[%d]: '%c' ", j - 1, desc[j - 1]);
            desc[j++] = '\\';//printf("d[%d]: '%c'\n", j - 1, desc[j - 1]);
        }
        else
        {
            desc[j++] = src[i];//printf("d[%d]: '%c'\n", j - 1, desc[j - 1]);
        }
    }
    desc[j] = '\0';
}



struct Result get_compile_error_result(struct Submit submit, int state, char * complie_result)
{
    struct Result result;
    result.submitID = submit.submitID;
    result.compileResult = state;
    strcpy(result.compilerOutput, complie_result);
    return result;
}



void load_configuration()
{
    FILE *fp = fopen("oj.ini", "r+");
    char line[LINE_BUF_SIZE];

    while (fgets(line, LINE_BUF_SIZE, fp) != NULL)
    {
        int i = 0;
        while (isspace(line[i])) i++;

        if (strlen(line) > 0 && line[0] != '\n' && line[i] != '#')
        {
            char *start = NULL;
            if ((start = strstr(line, "debug")) != NULL)
                sscanf(start, "%*s%d", &DEBUG);

            if ((start = strstr(line, "port")) != NULL)
                sscanf(start, "%*s%d", &port);

            if ((start = strstr(line, "output_file_size")) != NULL)
                sscanf(start, "%*s%d", &output_file_size);

            if ((start = strstr(line, "oj_home")) != NULL)
            {
                sscanf(start, "%*s%s", oj_home);
                /**/
                if (strlen(oj_home) == 0 || opendir(oj_home) == NULL)
                    getcwd(oj_home, 256);
            }

            if ((start = strstr(line, "restricted_syscalls")) != NULL)
            {
                //read debug
                char syscalls[LINE_BUF_SIZE];
                sscanf(start, "%*s%[^\n]", syscalls);
                //read restrict syscalls
                int cnt = 0;
                char * pch;
                pch = strtok (syscalls," ,");
                while (pch != NULL)
                {
                  strcpy(restrict_syscalls[cnt++], pch);
                  pch = strtok (NULL, " ,");
                }
                strcpy(restrict_syscalls[cnt], "");
            }
        }
    }
}



void print_configuraton()
{
    puts("=================comfiguration===================");
    printf("debug: %d\n", DEBUG);
    printf("port: %d\n", port);
    printf("output_file_size: %d\n", output_file_size);
    printf("oj_home: %s\n", oj_home);
    puts("restricted_syscalls:");
    for (int i = 0; strlen(restrict_syscalls[i]) > 0; i++)
        printf("%d: %s\n", 1 + i, restrict_syscalls[i]);
    puts("=================================================");
}



char *lowercase(char *src, char *desc)
{
    char *p = src;
    if (desc != NULL)
    {
        int i = 0;
        for (; i < strlen(src); i++)
            desc[i] = tolower(src[i]);
        desc[i] = 0;
        return desc;
    }

    for ( ; *p; ++p)
        *p = tolower(*p);
    return src;
}



struct Submit convert_submit_json(const char * submit_json)
{
    char json[strlen(submit_json)];
    strcpy(json, submit_json);
    for (int i = 0; i < strlen(json); i++)
        if (json[i] == ':' || json[i] == '{' || json[i] == '}' || json[i] == '\"' || json[i] == ',')
            json[i] = ' ';

    char str_language[16];
    struct Submit submit;
    sscanf(strstr(json, "submitID"),            "%*s%d", &submit.submitID);
    sscanf(strstr(json, "language"),            "%*s%s", str_language);
    sscanf(strstr(json, "timeLimit"),           "%*s%d", &submit.timeLimit);
    sscanf(strstr(json, "memLimit"),            "%*s%d", &submit.memLimit);
    sscanf(strstr(json, "runningFolder"),       "%*s%s", submit.runningFolder);
    sscanf(strstr(json, "testPointDataFolder"), "%*s%s", submit.testPointDataFolder);

    //将字符串表示的语言转为数字表示, 方便后面的比较
    lowercase(str_language, (char *) NULL); //convert to lowercase
    if (strcmp(str_language, "c") == 0)
        submit.language = LANG_C;

    if (strcmp(str_language, "cpp") == 0 || strcmp(str_language, "c++") == 0)
        submit.language = LANG_CPP;

    if (strcmp(str_language, "java") == 0)
        submit.language = LANG_JAVA;

    if (strcmp(str_language, "python2") == 0)
        submit.language = LANG_PYTHON2;

    if (strcmp(str_language, "python3") == 0)
        submit.language = LANG_PYTHON3;

    return submit;
}



int end_with(const char *file_name, const char *suffix)
{
    if (file_name == NULL || suffix == NULL)
        return 0;

    int file_name_len = (int) strlen(file_name);
    int suffix_len    = (int) strlen(suffix);

    if (file_name_len < suffix_len)
        return 0;

    while (suffix[--suffix_len] == file_name[--file_name_len]);
    return suffix_len < 0;
}



int start_with(const char *src, const char *pattern)
{
    int i = 0, j = 0;
    while (isspace(src[i])) i++;
    //printf("nonespace start at: %d\n", i);
    while (src[i] && pattern[j] && src[i] == pattern[j]) {i++; j++;}
    return j == strlen(pattern);
}



char *trim_tail(char *string)
{
    int j;
    int str_len = strlen(string);
    /*删除行末空格与换行*/
    for (j = str_len - 1; j >= 0 && (isspace(string[j]) || string[j] == '\n'); j--);
    string[j + 1] = '\0';
    return string;
}



void get_file_name(char *path, char *file_name, int with_suffix)
{
    if (path == NULL)
        return;

    int len = strlen(path);
    int start_pos = len, end_pos = len;

    if (with_suffix == 0)
        while (end_pos >= 0 && path[--end_pos] != '.'); //search . in string from end to start

    if (end_pos == -1) end_pos = len; //if . doesn't included, set end_pos to end

    while (start_pos >= 0 && path[--start_pos] != '/');//search / in string from end to start

    int i = 0;

    start_pos++;
    while (start_pos < end_pos)
        file_name[i++] = path[start_pos++]; //copy file name to buffer
    file_name[i] = '\0';
}



long get_file_size(const char *path)
{
    struct stat statbuff;
    if(stat(path, &statbuff) < 0)
        return -1;
    else
        return statbuff.st_size;
}



int copy_file(char *src, char *desc)
{
    int src_fd = open(src, O_RDONLY);
    int desc_fd = open(desc, O_CREAT | O_WRONLY | O_TRUNC, S_IRUSR | S_IWUSR | S_IRGRP | S_IWGRP | S_IROTH | S_IWOTH);

    if (desc_fd < 0 || src_fd < 0)
    {
        perror("open in copy_file");
        return 0;
    }

    char buf[BUF_SIZE];
    int read_len = 0;
    while ( (read_len = read(src_fd, buf, BUF_SIZE)) > 0)
    {
        if (write(desc_fd, buf, read_len) != read_len)
        {
            perror("write in copy_file");
            return 0;
        }
    }
    return 1;
}



int get_java_mem_usage(struct rusage usage)
{
    //java use pagefault
    int m_minflt;
    //printf("pagefault: %d, pagesize: %d\n",usage.ru_minflt,  getpagesize());
    m_minflt = usage.ru_minflt * getpagesize() / 1024;
    return m_minflt;
}



int get_mem_usage(pid_t pid, char *pattern)
{
    char status_path[32];
    int fd;
    sprintf(status_path, "/proc/%d/status", pid);
    if ((fd = open(status_path, O_RDONLY)) < 0)
        return 0;

    const int buf_len = 2048;
    char buf[buf_len];

    read(fd, buf, buf_len);
    close(fd);

    int val = 0;
    char *start = strstr(buf, pattern);
    if (start)
        sscanf(start, "%*s %d", &val);
    return val;
}



int get_time_consume(struct rusage usage)
{
    int time_consume  = (usage.ru_utime.tv_sec * 1000 + usage.ru_utime.tv_usec / 1000);
        time_consume += (usage.ru_stime.tv_sec * 1000 + usage.ru_stime.tv_usec / 1000);
    return time_consume;
}



int check_restrict_syscall(char *syscall_name)
{
    for (int i = 0; strlen(restrict_syscalls[i]) > 0; i++)
        if (strcmp(restrict_syscalls[i], syscall_name) == 0)
            return 1;
    return 0;
}



int check_test_points(char *running_folder, char test_points[][256])
{
    char input_data_path[256] = {0};
    char output_data_path[256] = {0};
    sprintf(input_data_path, "%s/input", running_folder);//构建输入数据目录
    sprintf(output_data_path, "%s/output", running_folder);//构建输出数据目录

    printf("input data: %s\n", input_data_path);
    printf("output data: %s\n", output_data_path);

    DIR *dir = opendir(input_data_path);
    if (dir == NULL) {
        perror("opendir");
        return -1;
    }

    struct dirent * dir_item;
    int test_point_count = 0;

    while ( (dir_item = readdir(dir)) != NULL)
    {
        //读取输入文件名, 读取输出文件目录, 查找是否由对应的输出文件
        if ( !end_with(dir_item->d_name, ".")  &&  end_with(dir_item->d_name, ".in")  )
        {
            char input_file[256] = {0};
            sprintf(input_file, "%s/%s", input_data_path, dir_item->d_name);

            //printf("get  in file: %s\n", input_file);

            if (end_with(dir_item->d_name, ".in"))
            {
                char file_name[256] = {0};
                get_file_name(dir_item->d_name, file_name, 0);
                //构建对应输出的路径
                char output_file[256] = {0};
                sprintf(output_file, "%s/%s.out", output_data_path, file_name);
                printf("get out file: %s\n", output_file);
                //如果有对应的输出文件, 则加入到测试点列表
                if (access(output_file, F_OK) != -1)
                    strcpy(test_points[test_point_count++], input_file);
            }
        }
    }
    return test_point_count;
}



int check_answer(const char *standard_output, const char *user_output)
{
    printf("compare:\n%s\n%s\n", standard_output, user_output);
    int std_ans_index = 0;
    int user_ans_index = 0;
    char std_ans_trim[output_file_size];
    char user_ans_trim[output_file_size];

    // 按行读取文件, 删除行末尾空格比较
    FILE *fp_std  = fopen(standard_output, "r");
    FILE *fp_user = fopen(user_output, "r");

    int buffer_len = 100;
    char std_buffer[buffer_len];
    char user_buffer[buffer_len];
    int flag = 0;
    /*读取一行标准答案, 然后读取一行用户答案进行比较*/
    while (fgets(std_buffer, buffer_len, fp_std) != NULL)
    {
        fgets(user_buffer, buffer_len, fp_user);
        char *std = trim_tail(std_buffer); //删除标准输出行末尾空格
        char *user = trim_tail(user_buffer); //删除用户输出行末尾空格
        //printf("cmp: \nstd[%s]\n", std);printf("usr[%s]\n\n", user);
        if (strcmp(std, user) != 0)
            flag = 1;

        //asc码可见字符在33到126之间
        for (int i = 0; i < strlen(std); i++)
            if (std[i] >= 33 && std[i] <= 126)
                std_ans_trim[std_ans_index++] = std[i];

        for (int i = 0; i < strlen(user); i++)
            if (user[i] >= 33 && user[i] <= 126)
                user_ans_trim[user_ans_index++] = user[i];
    }
    //检查对比完标准答案之后, 在没有出错的情况下再检查用户是否还有输出
    if (flag == 1)
        return WRONG_ANSWER;

    while (fgets(user_buffer, buffer_len, fp_user) != NULL)
    {
        char *user = trim_tail(user_buffer); //删除用户输出行末尾空格和换行
        //printf("exitra: [%s]\n", user);
        if (strlen(user) != 0)//忽略多输出的换行
            flag = 1;

        for (int i = 0; i < strlen(user); i++)
        {
            if (user[i] >= 33 && user[i] <= 126)
                user_ans_trim[user_ans_index++] = user[i];
        }
    }

    fclose(fp_user);
    fclose(fp_std);
    //printf("cmptrim: \nstd[%s]\n", std_ans_trim); printf("usr[%s]\n\n", user_ans_trim);

    if (flag == 0)
        return ACCEPTED;
    else
    {
        if (strcmp(std_ans_trim, user_ans_trim) == 0)
            return PRESENTATION_ERROR;
        else
            return WRONG_ANSWER;
    }
}



int compile(const struct Submit submit, char *complie_result)
{
    /*
    return value:
        -1 denote: system error
        0  denote: compile successful
        other denote: failed to compile
    */
    char code_file_path[256] = {0};
    char out_file[256] = {0};

    sprintf(out_file, "%s/%s", submit.runningFolder, "Main");


    //使用绝对路径
    /*
    switch (submit.language)
    {
        case LANG_C:       sprintf(code_file_path, "%s/%s", submit.runningFolder, "Main.c");    break;
        case LANG_CPP:     sprintf(code_file_path, "%s/%s", submit.runningFolder, "Main.cpp");  break;
        case LANG_JAVA:    sprintf(code_file_path, "%s/%s", submit.runningFolder, "Main.java"); break;
        case LANG_PYTHON2:
        case LANG_PYTHON3: sprintf(code_file_path, "%s/%s", submit.runningFolder, "Main.py");   break;
    }
    */
    //使用相对路径
    switch (submit.language)
    {
        case LANG_C:       sprintf(code_file_path, "%s", "Main.c");    break;
        case LANG_CPP:     sprintf(code_file_path, "%s", "Main.cpp");  break;
        case LANG_JAVA:    sprintf(code_file_path, "%s", "Main.java"); break;
        case LANG_PYTHON2:
        case LANG_PYTHON3: sprintf(code_file_path, "%s", "Main.py");   break;
    }

    printf("compile: %s\n", code_file_path);

    const char * c_cmd[]       = { "gcc", code_file_path, "-o", out_file, "-O2", "-Wall", "-lm", "--static", "-std=c99", NULL};
    const char * cpp_cmd[]     = { "g++", code_file_path, "-o", out_file, "-O2", "-Wall", "-lm", "--static", NULL};
    const char * java_cmd[]    = { "javac", "-J-Xms64m", "-J-Xmx256m", code_file_path, NULL };
    const char * python2_cmd[] = { "python2","-m","py_compile", code_file_path, NULL };
    const char * python3_cmd[] = { "python3","-m","py_compile", code_file_path, NULL };


    int fd[2]; //两个文件描述符
    int ret = pipe(fd); //创建管道
    if (ret < 0)
    {
        perror("pipe()");
        return -1;
    }

    int pid = fork();
    if (pid < 0)
    {
        perror("fork in compile()");
        return -1;
    }
    else if (pid == 0)  //创建编译进程
    {
        close(fd[0]); //关闭读
        if (dup2(fd[1], 2) < 0) //将标准错误重定向到父进程的标准输入
        {
            perror("dup2");
            return -1;
        }

        switch (submit.language)
        {
            case LANG_C:       execvp(c_cmd[0],       (char * const *) c_cmd); break;
            case LANG_CPP:     execvp(cpp_cmd[0],     (char * const *) cpp_cmd); break;
            case LANG_JAVA:    execvp(java_cmd[0],    (char * const *) java_cmd); break;
            case LANG_PYTHON2: execvp(python2_cmd[0], (char * const *) python2_cmd); break;
            case LANG_PYTHON3: execvp(python3_cmd[0], (char * const *) python3_cmd); break;
        }
    }
    close(fd[1]); //关闭父进程写

    int status;
    if (waitpid(pid, &status, 0) < 0)
        perror("waitpid in compile");

    while (1)
    {
        //从管道读取编译错误信息
        char buf[BUF_SIZE] = {0};
        if (read(fd[0], buf, BUF_SIZE) <= 0)
            break;
        strcat(complie_result, buf);
    }

    if (WIFEXITED(status)) //进程正常退出
    {
        puts("编译进程正常结束");
        return WEXITSTATUS(status); //获取子进程的返回值, 如果通过编译返回0, 否则返回大于0
    }
}



struct Test_point_detail run_program(struct Submit submit, char *input_file)
{
    //最大CPU时间限制
    struct rlimit time_limit;
    /*struct Submit中接收到的时间是毫秒, 在此将毫秒转换为秒*/
    int t_sec = submit.timeLimit / 1000;
    t_sec = t_sec > 0 ? t_sec : 1;

    time_limit.rlim_cur = t_sec + 1; //软限制为规定时间+1s
    time_limit.rlim_max = 5 * t_sec; // max's value max greater than cur's value, oterwise can't capture SIGXCPU signal

    //最大输出, 在配置文件中设置
    struct rlimit file_limit;
    file_limit.rlim_cur = output_file_size;
    file_limit.rlim_max = 5 * output_file_size;


    char execute_file[FILE_PATH_SIZE] = {0};
    char output_file[FILE_PATH_SIZE]  = {0};
    char error_file[FILE_PATH_SIZE]   = {0};

    sprintf(output_file, "%s/%s", submit.runningFolder, "user.out");
    sprintf(error_file,  "%s/%s", submit.runningFolder, "error.out");

    switch (submit.language)
    {
        case LANG_C:
        case LANG_CPP:     sprintf(execute_file, "%s/%s", submit.runningFolder, "Main"); break;
        case LANG_PYTHON2:
        case LANG_PYTHON3: sprintf(execute_file, "%s/%s", submit.runningFolder, "Main.py"); break;
    }

    const char * c_cpp_cmd[]   = {execute_file, execute_file, NULL};
    const char * java_cmd[]    = {"java", "-cp", submit.runningFolder, "Main", NULL};
    const char * python2_cmd[] = {"python2", execute_file, NULL};
    const char * python3_cmd[] = {"python3", execute_file, NULL};


    int status;
    int pid = fork();
    if (pid < 0)
    {
        perror("fork in run");
    }
    else if (pid == 0)
    {
        nice(19);
        //设置CPU时间限制
        if (setrlimit(RLIMIT_CPU,  &time_limit) < 0)
            perror("setrlimit RLIMIT_CPU");

        //设置内存限制
        if (setrlimit(RLIMIT_FSIZE, &file_limit) < 0)
            perror("setrlimit RLIMIT_FSIZE");

        //重定向 stdin/stdout/stderr
        freopen(input_file,  "r", stdin);
        freopen(output_file, "w", stdout);
        freopen(error_file,  "w", stderr);

        alarm(0);
        alarm(t_sec + 1);//set timmer

        //设置跟踪子进程
        ptrace(PTRACE_TRACEME, 0, NULL, NULL);

        switch (submit.language)
        {
            case LANG_C:
            case LANG_CPP:     execvp(c_cpp_cmd[0], (char * const *) c_cpp_cmd); break;
            case LANG_JAVA:    execvp(java_cmd[0],  (char * const *) java_cmd); break;
            case LANG_PYTHON2: execvp(python2_cmd[0], (char * const *) python2_cmd); break;
            case LANG_PYTHON3: execvp(python3_cmd[0], (char * const *) python3_cmd); break;
        }
    }
    else
    {
        int max_mem = -1;
        int result_index = RUNNING;

        struct rusage usage;
        struct user_regs_struct regs;

        while (1)
        {
            if (wait4(pid, &status, 0, &usage) < 0)
                perror("wait4");

            //跟踪子程序使用了什么系统调用
            ptrace(PTRACE_GETREGS, pid, NULL, &regs); //if user program is a dead loop, orig_rax may be a negtive number, so need to check here
            if ((int) regs.orig_rax >= 0)
            {
                //printf("SystemCall id:%d, %s\n", regs.orig_rax, syscalls[(int) regs.orig_rax]);
                //检查系统调用是否在禁用列表
                if (check_restrict_syscall(syscalls[(int)regs.orig_rax]))
                {
                    //printf("==================user used restrict syscall: %s\n", syscalls[(int)regs.orig_rax]);
                    result_index = RUNTIME_ERROR;
                    ptrace(PTRACE_KILL, 0, NULL, NULL);
                    break;
                }
            }

            //获取运行中内存消耗的峰值
            int mem_peak;
            if (submit.language == LANG_JAVA)
                mem_peak = get_java_mem_usage(usage);//java使用缺页数*页面大小
            else
                mem_peak = get_mem_usage(pid, "VmPeak");//其他程序冲proc中文件获取

            //更新最大峰值
            max_mem = mem_peak > max_mem ? mem_peak : max_mem;


            //内存超限
            if (mem_peak > submit.memLimit)//设置内存限制上线
            {
                result_index = MEMORY_LIMIT_EXCEEDED;
                ptrace(PTRACE_KILL, 0, NULL, NULL);
                break;
            }

            //检查是否有标准错误的输出, 有则认为runtime error
            if (get_file_size(error_file) > 0)
            {
                result_index = RUNTIME_ERROR;
                ptrace(PTRACE_KILL, 0, NULL, NULL);
                break;
            }




            if(WIFEXITED(status)) //正常退出
                break;

            if (WIFSTOPPED(status))//用户程序暂停执行
            {
                int sig = WSTOPSIG(status);
                //printf("WIFSTOPPED signal:%d\n", sig);
                switch (sig) {
                    case SIGALRM: alarm(0);
                    case SIGXCPU: result_index = TIME_LIMIT_EXCEEDED;   ptrace(PTRACE_KILL, pid, NULL, NULL);  break;
                    case SIGXFSZ: result_index = OUTPUT_LIMIT_EXCEEDED; ptrace(PTRACE_KILL, pid, NULL, NULL); break;
                    case SIGFPE:
                    case SIGSEGV: result_index = RUNTIME_ERROR; ptrace(PTRACE_KILL, pid, NULL, NULL); break;
                }
            }



            if (WIFSIGNALED(status))//程序收到信号,异常退出
            {
                int sig = WTERMSIG(status);
                //printf("WIFSIGNALED signal:%d\n", sig);
                switch (sig) {
                    case SIGALRM: alarm(0);
                    case SIGXCPU: result_index = TIME_LIMIT_EXCEEDED;   break;
                    case SIGXFSZ: result_index = OUTPUT_LIMIT_EXCEEDED; break;
                    case SIGKILL: break;//kill什么都不做
                    default:      result_index = RUNTIME_ERROR;
                }
                ptrace(PTRACE_KILL, pid, NULL, NULL);
                break;
            }

            ptrace(PTRACE_SYSCALL, pid, NULL, NULL);
        }
        

        char file_name[FILE_PATH_SIZE] = {0};
        get_file_name(input_file, file_name, 0);

        //程序在规定时间加1000ms内正常结束, RUNNING状态不会更新
        if (result_index == RUNNING)
        {
            if (get_time_consume(usage) >= submit.timeLimit)
                result_index = TIME_LIMIT_EXCEEDED; //设置超时
            else
            {   //检查答案
                char standard_output_file[256] = {0};
                sprintf(standard_output_file, "%s/output/%s.out",  submit.runningFolder, file_name);
                result_index = check_answer(standard_output_file, output_file);
            }
        }

        puts("==========================================================");
        printf("  inputfile: %s\n", input_file);
        printf("     result: %s\n", result_list[result_index]);
        printf("timeConsume: %dms\n", get_time_consume(usage));
        printf(" memConsume: %dKB\n", max_mem);
        puts("==========================================================");

        struct Test_point_detail detail;
        detail.testPointID = atoi(file_name);
        detail.timeConsume = get_time_consume(usage);
        detail.memConsume  = max_mem;
        detail.returnVal   = 0;
        detail.result      = result_index;
        return detail;
    }
}


struct Result do_judge_work(struct Submit submit)
{
    /*这函数在运行过程中不更新当前运行状态, 直接返回最终结果*/
    puts("检查测试点文件");
    //prepare test point file
    char test_points[20][256];
    //复制测试点文件到运行目录
    int test_point_count = -1;

    test_point_count = check_test_points(submit.runningFolder, test_points);
    if (test_point_count == -1) //files are failed to ready,
        return get_system_error_result(submit, "复制测试点文件到运行目录出错!");


    puts("\n编译代码");
    //compile code file
    int compile_retval = 256;
    char complie_result[4 * 1024] = {0};
    char complie_result_json[4 * 1024] = {0};
    compile_retval = compile(submit, complie_result);
    printf("\n编译返回值: %d\n", compile_retval);
    printf("编译结果:\n%s\n", complie_result);
    format_string_for_json(complie_result, complie_result_json);

    if (compile_retval == -1)
        return get_system_error_result(submit, "编译过程中发生系统错误!");
    else if (compile_retval > 0)
        return get_compile_error_result(submit, compile_retval, complie_result_json);

    puts("\n运行用户程序");
    //run user program and test each test point
    struct Result result;
    result.testPointCount = test_point_count;
    result.submitID = submit.submitID;
    result.compileResult = compile_retval;
    strcpy(result.compilerOutput, complie_result_json);

    //run for each test point
    for (int i = 0; i < test_point_count; i++)
    {
        //printf("%s\n", test_points[i]);
        result.detail[i] = run_program(submit, test_points[i]);
    }
    return result;
}


void do_judge_work_update_state(int client_socket)
{
    /*这函数在运行过程中更新当前运行状态*/
    //以下4个常量用户与web服务器交换状态
    const char *SUBMIT_STATE = "submit";
    const char *WAIT_STATE   = "wait";
    const char *FINISH_STATE = "finish";
    const char *STATE        = "state";

    char response_str[8*1024] = {0};
    char string_buffer[8*1024] = {0};
    char json_buffer[8*1024] = {0};


    int test_point_count = 0; //保存测试点数量, 同遍历文件获取
    int compile_retval = 256; //编译结果返回值
    char test_points[20][256] = {0};//每个测试点的名字, 上限20个测试点, 超出会溢出
    char complie_result[4 * 1024] = {0};      //保存原始编译结果
    char complie_result_json[4 * 1024] = {0}; //保存将特殊字符转义后的编译结果, 否则无法放如json中传输

    struct Submit submit; //将web server提交的json转换为结构体后保存在此

    /*
    以下代码需要和web服务器交换信息多次, 以更新评测状态, 最多不超过三次
    第一次: 接受web server提交的json
    中间次: 更新评测机当前的状态, 如果遇到错误, 则终止后续的状态更新
    最后一次: 发送评测结果
    */

    //第一次读取web服务器提交的json字符串, 并准备测试点文件, 更新状态为正在编译
    read(client_socket, string_buffer, sizeof(string_buffer));
    if (start_with(string_buffer, SUBMIT_STATE)) //检查字符串的前置标志
    {
        //
        printf("<<<server to judge[SUBMIT]: %s\n", string_buffer);
        submit = convert_submit_json(string_buffer + strlen(SUBMIT_STATE));
        print_submit(submit);
        chdir(submit.runningFolder);

        /*char ttt[256];
        getcwd(ttt, 256);
        printf("change work dir to: %s\n", ttt);*/

        puts("check test points");

        //
        test_point_count = check_test_points(submit.runningFolder, test_points);

        if (test_point_count == -1) //复制文件遇到错误
        {
            //生成系统错误信息
            get_result_json(json_buffer, get_system_error_result(submit, "复制测试点文件遇到错误!"));

            memset(response_str, 0, sizeof(response_str));//清空缓冲区
            sprintf(response_str, "%s %s", FINISH_STATE, json_buffer); //构建相应字符串, 发生错误直接终止后续工作

            printf(">>>judge to server[FINISH_STATE]: %s\n", response_str);
            write(client_socket, response_str, sizeof(response_str));
            return;
        }
        else //成功复制测试点文件, 更新状态为正在编译
        {
            get_judge_state_json(submit, json_buffer, COMPILING);
            memset(response_str, 0, sizeof(response_str));
            sprintf(response_str, "%s %s", STATE, json_buffer);

            printf(">>>judge to server[STATE]: %s\n", response_str);
            write(client_socket, response_str, sizeof(response_str));
        }
    }



    /*编译代码*/
    //等待web服务器收到消息之后的响应
    memset(string_buffer, 0, sizeof(string_buffer));
    read(client_socket, string_buffer, sizeof(string_buffer));
    printf("<<<server to judge[WAIT]: %s\n", string_buffer);
    if (start_with(string_buffer, WAIT_STATE))
    {
        //开始编译文件
        compile_retval = compile(submit, complie_result);
        printf("======================>compile retval: %d\n", compile_retval);
        printf("======================>compile result: %s\n", complie_result);

        //清空响应字符串缓冲区
        memset(response_str, 0, sizeof(response_str));

        if (compile_retval == -1)
        {
            //编译过程中发生系统错误
            get_result_json(json_buffer, get_system_error_result(submit, "编译过程中发生系统错误!"));
            sprintf(response_str, "%s %s", FINISH_STATE, json_buffer); //设置终止标志

            printf(">>>judge to server[FINISH_STATE]: %s\n", response_str);
            write(client_socket, response_str, sizeof(response_str));
            return;
        }
        else if (compile_retval == 0)
        {
            //编译成功
            get_judge_state_json(submit, json_buffer, RUNNING);//更新状态为正在运行
            sprintf(response_str, "%s %s", STATE, json_buffer);//填充缓冲区

            printf(">>>judge to server[STATE]: %s\n", response_str);
            write(client_socket, response_str, sizeof(response_str));
        }
        else
        {
            //编译错误
            format_string_for_json(complie_result, complie_result_json);//对编译信息特殊字符进行转义
            //生成结果json
            get_result_json(json_buffer, get_compile_error_result(submit, compile_retval, complie_result_json));
            sprintf(response_str, "%s %s", FINISH_STATE, json_buffer);//设置终止标志,返回给web服务器

            printf(">>>judge to server[FINISH]: %s\n", response_str);
            write(client_socket, response_str, sizeof(response_str));
            return;
        }
    }



    /*运行代码*/
    //读取服务器的响应状态
    memset(string_buffer, 0, sizeof(string_buffer));
    read(client_socket, string_buffer, sizeof(string_buffer));
    printf("<<<server to judge[WAIT]: %s\n", string_buffer);
    if (start_with(string_buffer, WAIT_STATE))
    {
        //所有工作就绪,运行用户程序
        struct Result result; //保存评测结果
        result.testPointCount = test_point_count; //设置测试点, 用于生成json
        result.submitID = submit.submitID; //设置提交ID
        result.compileResult = compile_retval; //设置编译返回值

        format_string_for_json(complie_result, complie_result_json); //格式化编译结果
        strcpy(result.compilerOutput, complie_result_json); //设置编译结果

        //针对每一个测试点运行一次程序
        for (int i = 0; i < test_point_count; i++)
            result.detail[i] = run_program(submit, test_points[i]);//保存每个测试点的运行结果

        get_result_json(json_buffer, result);//将评测结果转为json字符串
        memset(response_str, 0, sizeof(response_str));
        sprintf(response_str, "%s %s", FINISH_STATE, json_buffer);


        printf(">>>judge to server[FINISH]: %s\n", response_str);
        write(client_socket, response_str, sizeof(response_str));
    }
}


void create_daemon()
{
    int pid = fork();
    if (pid < 0)
        perror("fork");
    else if (pid > 0)
        exit(0);
    else
    {
        if (setsid() < 0)
        {
            perror("setsid");
            exit(0);
        }
        chdir(oj_home);
        //关闭文件
        /*for (int i = 0; i <= 2; i++)
            close(i);*/

        umask(0);
    }
}



int main()
{

    load_configuration();
    print_configuraton();

    char submit_json[8 * 1024];
    char judge_result_json[8 * 1024];

    setbuf(stdout,NULL);

    //这段代码用户测试是否能正常评测代码
    if (0 == 1)
    {
        //strcpy(submit_json, "{\"submitID\":20,\"language\":\"c\",\"timeLimit\":1,\"memLimit\":65535,\"runningFolder\":\"/home/xanarry/Desktop/running-dir/submit20\",\"testPointDataFolder\":\"/home/xanarry/Workspace/IdeaProjects/oj/out/artifacts/oj_war_exploded/test-points/p1007\"}");
        strcpy(submit_json, "{\"submitID\":127,\"language\":\"C\",\"timeLimit\":1000,\"memLimit\":65535,\"runningFolder\":\"/home/xanarry/Desktop/running-dir/submit127\",\"testPointDataFolder\":\"/home/xanarry/Workspace/IdeaProjects/oj/out/artifacts/oj_war_exploded/test-points/p1007\"}");
        struct Submit submit = convert_submit_json(submit_json);
        printf("submit: %s\n\n", submit_json);
        chdir(submit.runningFolder);
        struct Result result = do_judge_work(submit);

        get_result_json(judge_result_json, result);
        printf("\nresult: %s\n\n", judge_result_json);
        chdir(oj_home);
        return 0;
    }


    //是否创建守护进程
    create_daemon();


    int update_state = 1;


    /*  创建服务端套接字,
        AF_INET表示使用ipv4类型的地址,
        SOCK_STREAM 表示使用面向连接的数据传输方式
        IPPROTO_TCP 表示使用 TCP 协议
    */
	int server_socket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);

     //设置端口复用, 使得从新打开程序后能立即使用端口
    int mw_optval = 1;
    setsockopt(server_socket, SOL_SOCKET, SO_REUSEADDR, (char *)&mw_optval, sizeof(mw_optval));

	/*构建服务端的地址, IP地址和端口都保存在 sockaddr_in 结构体中*/
	struct sockaddr_in server_addr;
	memset(&server_addr, 0, sizeof(server_addr));//空位的内存用0补充
	server_addr.sin_family = AF_INET;//设置地址类型为ipv4
	server_addr.sin_addr.s_addr = INADDR_ANY;//设置ip
	server_addr.sin_port = htons(2345);//设置端口


	//bind() 函数将套接字 serv_sock 与特定的IP地址和端口绑定, 此后通过ip+port的连接相当与直接此套接字
	bind(server_socket, (struct sockaddr *) &server_addr, sizeof(server_addr));
	//让套接字处于被动监听状态。指套接字一直处于“睡眠”中，直到客户端发起请求才会被“唤醒”。
	listen(server_socket, 20);

	struct sockaddr_in client_addr;
	socklen_t client_addr_size = sizeof(client_addr);

    while (1) //始终等待提交
    {
        //accept connection
        puts("\n\n\n等待新的提交");
        if (update_state == 1)
        {
            chdir(oj_home);
            int client_socket = accept(server_socket, (struct sockaddr *) &client_addr, &client_addr_size);
            do_judge_work_update_state(client_socket);
        }
        else
        {
            chdir(oj_home);
            char string_buffer[8 * 1024] = {0};
            int client_socket = accept(server_socket, (struct sockaddr *) &client_addr, &client_addr_size);
            read(client_socket, string_buffer, sizeof(string_buffer));
            printf("get: %s\n", string_buffer);


            struct Submit submit = convert_submit_json(submit_json);//转换json为结构
            print_submit(submit);

            struct Result result = do_judge_work(submit);
            get_result_json(judge_result_json, result);//将评测结果转为json字符串
            write(client_socket, judge_result_json, sizeof(judge_result_json));
        }
    }


    close(server_socket);
    return 0;
}



/*
submitJson
{"submitID":20,"language":"C","timeLimit":1000,"memLimit":65535,"runningFolder":"/home/xanarry/Desktop/running-dir/submit20","testPointDataFolder":"/home/xanarry/Workspace/IdeaProjects/oj/out/artifacts/oj_war_exploded/test-points/p1007"}


/*
 1) SIGHUP       2) SIGINT       3) SIGQUIT      4) SIGILL       5) SIGTRAP
 6) SIGABRT      7) SIGBUS       8) SIGFPE       9) SIGKILL     10) SIGUSR1
11) SIGSEGV     12) SIGUSR2     13) SIGPIPE     14) SIGALRM     15) SIGTERM
16) SIGSTKFLT   17) SIGCHLD     18) SIGCONT     19) SIGSTOP     20) SIGTSTP
21) SIGTTIN     22) SIGTTOU     23) SIGURG      24) SIGXCPU     25) SIGXFSZ
26) SIGVTALRM   27) SIGPROF     28) SIGWINCH    29) SIGIO       30) SIGPWR
31) SIGSYS      34) SIGRTMIN    35) SIGRTMIN+1  36) SIGRTMIN+2  37) SIGRTMIN+3
38) SIGRTMIN+4  39) SIGRTMIN+5  40) SIGRTMIN+6  41) SIGRTMIN+7  42) SIGRTMIN+8
43) SIGRTMIN+9  44) SIGRTMIN+10 45) SIGRTMIN+11 46) SIGRTMIN+12 47) SIGRTMIN+13
48) SIGRTMIN+14 49) SIGRTMIN+15 50) SIGRTMAX-14 51) SIGRTMAX-13 52) SIGRTMAX-12
53) SIGRTMAX-11 54) SIGRTMAX-10 55) SIGRTMAX-9  56) SIGRTMAX-8  57) SIGRTMAX-7
58) SIGRTMAX-6  59) SIGRTMAX-5  60) SIGRTMAX-4  61) SIGRTMAX-3  62) SIGRTMAX-2
63) SIGRTMAX-1  64) SIGRTMAX
*/
