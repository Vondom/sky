namespace java com.sky.commons

struct KClass {
  1: string packageName,
  2: string className
}

struct KMethod {
  1: KClass classKey,
  2: string methodName,
  3: string signature
}

struct KThrowable {
  1: KClass classKey,
  2: string message
}

struct MethodProfile {
  1: KMethod callee,
  2: KMethod caller,
  3: KThrowable throwable,
  4: i64 elapsedTime,
  5: i64 profileId,
  6: i64 timestamp,
  7: i64 index,
  8: string threadName,
  9: i64 projectId
}

service AgentControlService {
  i64 createProfile(1: i64 workId),
  oneway void put(1:MethodProfile methodProfile)
}

service WorkerControlService {
  i64 add(1: string address, 2: i32 port),
  void done(1: i64 id),
  void remove(1: i64 id)
}