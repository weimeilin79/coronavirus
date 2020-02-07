from("knative:channel/unknown-handler")
.to('log:info?showAll=false')
.to('kafka:my-topic?brokers=my-cluster-kafka-bootstrap.streams.svc:9092')
