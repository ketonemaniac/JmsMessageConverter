# JMS Message Convertor

Using the good old Spring JMS to receive/convert messages (since actually the real thing is on some JMS 2.0 compliant MQ)

### Reference Documentation
Modified from the tutorial https://tanzu.vmware.com/content/blog/messaging-with-jms-and-rabbitmq


## Rabbit MQ 
https://stackoverflow.com/questions/52819237/how-to-add-plugin-to-rabbitmq-docker-image

- Prepare custom image:

#### Dockerfile

  FROM rabbitmq:3.7.18-management
  RUN rabbitmq-plugins enable rabbitmq_delayed_message_exchange

#### docker-composer.yml

rabbitmq:
  image: rabbitmq-custom
  ports:
    - "5672:5672"
    - "15672:15672"

- Build the image

docker build -t rabbitmq-custom .

- Run the docker composer:

docker-compose up