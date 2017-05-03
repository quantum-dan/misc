#include <stdio.h>

typedef struct MaybeInt {
  int exists;
  int value;
} MaybeInt;

MaybeInt bind(MaybeInt m, MaybeInt (*f)(int)) {
  if (m.exists) return f(m.value);
  else return m;
}

int printMaybe(MaybeInt m) {
  if (m.exists) printf("Just %d\n", m.value);
  else printf("Nothing\n");
  return 0;
}

MaybeInt f(int input) {
  MaybeInt r;
  if (input) {
    r.exists = 1;
    r.value = 2 * input;
  } else {
    r.exists = 0;
  }
  return r;
}

int runTests() {
  MaybeInt a = {.exists = 1, .value = 3};
  MaybeInt b = {.exists = 0, .value = 2};
  MaybeInt c = {.exists = 1, .value = 0};
  printMaybe(a); printMaybe(b); printMaybe(c);
  printMaybe(bind(a, f)); printMaybe(bind(b, f)); printMaybe(bind(c, f));
  return 0;
}

int main() {
  return runTests();
}
