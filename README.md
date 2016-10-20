# any-mailer-core

A server-side platform for value added services via email.
Users can consult internet info by sending emails with specific subjects that the plaform uses to route the message to the appropriate service.

## How to build

Use to build:

```
gradlew build
```

Use to deploy:

```
gradlew deployToTomcat
```

## How to start and stop the service

Go to the **index.html** page and hit on the **Iniciar servicio** button to start the service. 
Hit the **Detener servicio*** button to stop it.

## How to use

* Send an email to the predefined email account.
* Write the name of the service on the *Subject*
* Write the topics to search in the *Message Body*
* Wait until receive a response email

## Currently implemented services

* Revolico
* Stackoverflow
* Download