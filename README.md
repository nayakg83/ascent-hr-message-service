# Ascent HR Message Service

## Create Message Queue:

### POST API: http://localhost:3106/messages/queues?queueName=test

### Response:
{
    "id": "aad79d3a-a6b7-4710-ba05-db90ebab262c",
    "name": "test",
    "size": 0,
    "messages": []
}

## Update Message Queue:

### PUT API: http://localhost:3106/messages/queues/aad79d3a-a6b7-4710-ba05-db90ebab262c?queueName=test1

### Response:
{
    "id": "aad79d3a-a6b7-4710-ba05-db90ebab262c",
    "name": "test1",
    "size": 0,
    "messages": []
}


## Get Message Queue Details:

### GET API: http://localhost:3106/messages/queues/aad79d3a-a6b7-4710-ba05-db90ebab262c

### Response:
{
    "id": "aad79d3a-a6b7-4710-ba05-db90ebab262c",
    "name": "test1",
    "size": 0,
    "messages": []
}


## Delete Message Queue:

### DELETE API: http://localhost:3106/messages/queues/aad79d3a-a6b7-4710-ba05-db90ebab262c

### Response: Messgae queue deleted!

## Enqueque Message:

### POST API: http://localhost:3106/messages/enqueue/968a6d43-e19d-4a0d-93ec-451c53f7781f
          
Request Body:
{
"content": "test1"
}

### Response:
{
    "id": "968a6d43-e19d-4a0d-93ec-451c53f7781f",
    "name": "test",
    "size": 1,
    "messages": [
        {
            "id": "109ad097-1dae-48fc-bf07-ec02b48ed88e",
            "createdDate": "08/09/2019 10:03",
            "retryAttempts": 0,
            "deliveryStatus": "READY",
            "body": "dGVzdDE="
        }
    ]
}

## Dequeue Message:

### PUT API: http://localhost:3106/messages/dequeue/968a6d43-e19d-4a0d-93ec-451c53f7781f

### Response:
{
    "id": "968a6d43-e19d-4a0d-93ec-451c53f7781f",
    "name": "test",
    "size": 0,
    "messages": []
}

## Peek Messages:

### GET API: http://localhost:3106/messages/peek/968a6d43-e19d-4a0d-93ec-451c53f7781f?messageId=4a195b80-36f0-4333-a806-94d83b4362f7

### Response:
{
    "id": "4a195b80-36f0-4333-a806-94d83b4362f7",
    "createdDate": "08/09/2019 10:09",
    "retryAttempts": 0,
    "deliveryStatus": "READY",
    "body": "dGVzdDE="
}

## Purge Messages:

### PUT API: http://localhost:3106/messages/purge/968a6d43-e19d-4a0d-93ec-451c53f7781f

### Response: Messages purged!



## Database Details:
This application runs against postgres used as RDBMS. Please use the below command in linux system to run the postgres

Docker command: docker run --name some-postgres -e POSTGRES_PASSWORD=postgres -d -p 5432:5432 postgres

## To create application jar from the source code, run the following command:

mvn package

## Run application jar:

Run application command: java -jar ascent-hr-message-service-0.0.1-SNAPSHOT.jar 









