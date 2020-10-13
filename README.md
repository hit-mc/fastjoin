# FastJoin - A Minecraft mod that lets you join server a lot faster

## Setup

Just put the .jar file into mods folder.

Note: You need Fabric Loader.

## How it works

The way that Minecraft connects to a Minecraft server includes a redunant host name reverse lookup, which lead to significant (~20s) lag when connecting to a Minecraft server, which does not have a domain.

This mod simply avoid the reverse lookup: If you provide an IP address, then just connect to it. If you provide a domain, we get the IP it points to, then connect to the IP address.