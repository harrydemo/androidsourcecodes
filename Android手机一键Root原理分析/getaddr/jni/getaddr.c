#include <dlfcn.h>
#include <stddef.h>
#include <stdio.h>

int main(void)
{
    void*  lib = dlopen("libc.so", RTLD_NOW | RTLD_GLOBAL);
    void*  symbol;

    if (lib == NULL) {
        fprintf(stderr, "Could not open self-executable with dlopen(NULL) !!: %s\n", dlerror());
        return 1;
    }
    symbol = dlsym(lib, "exit");
    if (symbol == NULL) {
        fprintf(stderr, "Could not lookup symbol exit !!: %s\n", dlerror());
        return 2;
    }
	printf("exit() addr:%08x\n", symbol);
	symbol = dlsym(lib, "setresuid");
    if (symbol == NULL) {
        fprintf(stderr, "Could not lookup symbol setresuid !!: %s\n", dlerror());
        return 2;
    }
	printf("setresuid() addr:%08x\n", symbol);
    dlclose(lib);
    return 0;
}
