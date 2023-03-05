# Hardcore Lives

Minecraft server plugin that makes you a limited amount of lives _(by default 3)_.

Once you lose all your lives you revive as a spectator.

### Configurations
These are the configurations that can be set up on config.yml.

| Key   | Description                   |
|-------|-------------------------------|
| lives | Number of lives before dying. |

### Commands
There are some commands you can use to manage and retrieve players lives.

* `/hcl get <player>`: Gets the player lives left.
* `/hcl set <player> <lives>`: Sets a player lives left to the given ones. 


### Permission nodes
Permissions nodes that can be set with a permission manager like LuckPerms.

- `hardcorelives.*`: Gives access to all commands.
- `hardcorelives.get`: Gives access to `/hcl get`.
- `hardcorelives.set`: Gives access to `/hcl set`.

### Placeholders
When using the PlaceholderAPI in conjunction with this plugin you will have the following placeholders at your disposal.

- `%hardcorelives_lives%`: Player lives left.
- `%hardcorelives_starting_lives%`: Players default starting number of lives. These are the `lives` defined in the `config.yml`
