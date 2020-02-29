# fresta: a Freenet REST API

The Freenet REST API is an attempt to let clients access Freenet using a simple
API for which more clients are available. It is also hoped that client
application can use this API to access a Freenet node on a remote node, using
an SSL-secured and authenticated reverse proxy between the client and the node,
e.g. on mobile phones.

# Current State

At the moment Fresta offers 3 endpoints: GetConfig, SetConfig, and
GenerateKeypair.

## GetConfig

This returns the node’s complete configuration.

> GET /config

## SetConfig

This lets you set configuration options on the node.

> PUT /config  
> Body: `{"node.option1":"value1","node.option2":"value2"}`

## GenerateKeypair

This generates an SSK key pair.

> GET /keypair

# Compiling

`./gradlew run` should take care of most things. You might have to install
jFCPlib manually (i.e. check out its `next` branch, run `mvn install` to
install it into your local maven repository).

A web server will be started on port 7777, and it will talk to a Freenet node
on `localhost`, port 9481 (the default FCP port). These settings are not
configurable outside of the source code yet.
