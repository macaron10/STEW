ex)학생 1100명이 수강신청을 동시에 신청할 시 수강신청 먼저 요청한 순서대로 5초안에 하고 싶다??

Scaleout 가능 구조

DB Sharing (분산 저장 ?)

L4Switch LoadBalancer

MemoryCache (Cache Server : Redis)

MessageQueue



스케쥴링

DB Batch Job

Beackend Batch System

SpringBatch?? 있지않았나



컨설턴트님한테 물어보기



L4 switch -> WAS(s) Cache, batchServer -> DB (Sharing, Master / Slave)