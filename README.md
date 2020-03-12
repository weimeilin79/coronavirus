# Corona Virus Demo 

![](Outbreak.png)


## Folder Structure

```
--Setup
-- src
	|--VirusDispatcher	(Dispatch lab result to handlers through Channel)
	|--channel (Knative Channel to receive events)
		|--Alpha
		|--Mers
		|--Unknown
		|--Noval (COVID-19)
	|--handlers (Handlers that send notice to Dashboard by channel events)
		|--Alpha
		|--Mers
		|--Unknown
		|--Noval (COVID-19)
	|--Simulator
		|--SimulatorSend (Send fake lab data)
		|--Dashboard (Send notification to Dashboard)
		|--SimulatorCloudEvent (RC 1- Problem with two Camel K 
		operators)
-- ui
```
## Setup Base Environment
Install Operators

1. OpenShift Serverless Operator
1. Knative Eventing Operator
1. AMQ Streams
1. Camel K


Create Project to setup AMQ Streams(Kafka)

```
oc new-project streams

```

Create the Kafka Cluster cluster under streams project

```
apiVersion: kafka.strimzi.io/v1beta1
kind: Kafka
metadata:
  name: my-cluster
  namespace: streams
spec:
  kafka:
    version: 2.3.0
    replicas: 3
    listeners:
      plain: {}
      tls: {}
    config:
      offsets.topic.replication.factor: 3
      transaction.state.log.replication.factor: 3
      transaction.state.log.min.isr: 2
      log.message.format.version: '2.3'
    storage:
      type: ephemeral
  zookeeper:
    replicas: 3
    storage:
      type: ephemeral
  entityOperator:
    topicOperator: {}
    userOperator: {}

```

And also create the Kafka Topic 

```
apiVersion: kafka.strimzi.io/v1beta1
kind: KafkaTopic
metadata:
  name: my-topic
  labels:
    strimzi.io/cluster: my-cluster
  namespace: streams
spec:
  partitions: 10
  replicas: 3
  config:
    retention.ms: 604800000
    segment.bytes: 1073741824

```

And also create the Knative Serving if not existed.

```
apiVersion: v1
kind: Namespace
metadata:
 name: knative-serving
```

```
apiVersion: serving.knative.dev/v1alpha1
kind: KnativeServing
metadata:
 name: knative-serving
 namespace: knative-serving
                              
```

Create namespece for the demo

```
oc new-project outbreak

```

Create Camel K Integration Platform

```
apiVersion: camel.apache.org/v1
kind: IntegrationPlatform
metadata:
  name: example
  namespace: outbreak
spec: {}
```


## Install applications

Setup Dashboard

```
oc new-app quay.io/weimeilin79/myui:latest
oc expose service myui
```

Get your Dashboad location

```
oc get route
```


### Setup application

#### Existing virus outbreak handler

- Setup Channel, under src/channel

```
oc create -f channelalpha.yaml		
oc create -f channelmers.yaml
oc create -f channelunknown.yaml
oc create -f channelnoval.yaml		
```

- Install the existing virus outbreak handler, under src/handlers

```
kamel run -d camel-jackson AlphaHandler.java
kamel run -d camel-jackson MersHandler.yaml
kamel run -d camel-jackson UnknownHandler.groovy
```


- Start sending in lab result, under src/simulator

```
kamel run -d camel-jackson -d camel-bean SimulateSender.java 
kamel run Dashboard.java
```

- Start dispatching virus result to handlers, under src/

```
kamel run -d camel-jackson VirusDispatcher.java --dev
```

- Go to Dashboard to see the virus


#### Adding COVID-19 handler

- Install the new COVID19 outbreak handler, under src/handlers

```
kamel run -d camel-jackson NovalHandler.java
```

- Update your VirusDispatcher.java under src/ add the following condition ***(You should be using DEV mode)***

```
	      .when().simple("${body.genuses} == 'Novalvirus'")
             .marshal(jacksonDataFormat)
             .log("MERS - ${body}")
             .to("knative:channel/noval-handler")
```

- Go to Dashboard to see the new COVID 19 virus appears
