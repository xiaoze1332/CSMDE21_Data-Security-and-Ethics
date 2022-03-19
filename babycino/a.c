#include <stdio.h>
#include <stdlib.h>

union ilword {
    int n;
    union ilword* ptr;
    void(*f)();
};
typedef union ilword word;

word param[1];
int next_param = 0;

word r0 = {0};

word vg0 = {0};
word vg1 = {0};
word vg2 = {0};
void INIT();
void MAIN();
void Flow_l();
int main() {
    INIT();
    MAIN();
    return 0;
}

void INIT() {
    word vl[0];
    word r4 = {0};
    word r3 = {0};
    word r2 = {0};
    word r1 = {0};
    int p;
    for(p = 0; p <= -1 && p < 1; p++) {
        vl[p] = param[p];
    }
    next_param = 0;
INIT:
    r2.n = 0;
    vg0.ptr = calloc(r2.n, sizeof(word));
    r2.n = 0;
    vg1.ptr = calloc(r2.n, sizeof(word));
    r2.n = 1;
    vg2.ptr = calloc(r2.n, sizeof(word));
    r3 = vg2;
    r4.f = &Flow_l;
    *(r3.ptr) = r4;
    return;
}

void MAIN() {
    word vl[0];
    word r1 = {0};
    int p;
    for(p = 0; p <= -1 && p < 1; p++) {
        vl[p] = param[p];
    }
    next_param = 0;
MAIN:
    r1.n = 0;
    printf("%d\n", r1.n);
    return;
}

void Flow_l() {
    word vl[3] = {0,0,0};
    word r6 = {0};
    word r5 = {0};
    word r4 = {0};
    word r3 = {0};
    word r2 = {0};
    word r1 = {0};
    int p;
    for(p = 0; p <= 2 && p < 1; p++) {
        vl[p] = param[p];
    }
    next_param = 0;
Flow_l:
    r1.n = 10;
    r2.n = vl[1].n < r1.n;
    if (r2.n == 0) goto Flow_l_0;
    goto Flow_l_1;
Flow_l_0:
Flow_l_1:
    r5.n = 10;
    r6.n = vl[2].n < r5.n;
    if (r6.n == 0) goto Flow_l_2;
    goto Flow_l_3;
Flow_l_2:
Flow_l_3:
    r0.n = 0;
    return;
}

