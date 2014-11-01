namespace java com.sky.commons

enum State {
  WORKING,
  IDLE
}

exception WorkingException {

}

struct Jar {
  1: binary file,
  2: string name
}

struct Work {
  1: Jar jar,
  2: string arguments,
  3: i64 id
}

struct Status {
  1: State state,
  2: i64 workerId
}

service Worker {
  string doWork(1: Work work) throws (1:WorkingException e),
  Status status()
}