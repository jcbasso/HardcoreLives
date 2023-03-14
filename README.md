# Hardcore Lives

Minecraft server plugin that makes you a limited amount of lives _(by default 3)_.

Once you lose all your lives you revive as a spectator.

### Configurations

These are the configurations that can be set up on config.yml.

| Key                                  | Description                                                                                                | Default |
|--------------------------------------|------------------------------------------------------------------------------------------------------------|---------|
| `lives`                              | Number of lives before dying.                                                                              | `3`     |
| `locale` [1](#language)              | Language to use for the messages.                                                                          | `en-US` |
| `actionbar.enabled` [2](#action-bar) | Defines weather to manage or not the lives custom action bar.                                              | `false` |
| `actionbar.hearts_color`             | Color of the hearts shown on action bar. Options: red/gold.                                                | `red`   |
| `actionbar.show_empty`               | Shows empty hearts when you have lost any. The total amount of hearts will be defined by `lives` variable. | `true`  |

### Commands

There are some commands you can use to manage and retrieve players lives.

* `/hcl get <player>`: Gets the player lives left.
* `/hcl set <player> <lives>`: Sets a player lives left to the given ones.
* `/hcl revive <player> [<lives>]`: Revives a player and set the lives either to the starting `lives` or the given
  number of lives.

### Permission nodes

Permissions nodes that can be set with a permission manager like LuckPerms.

- `hardcorelives.*`: Gives access to all commands.
- `hardcorelives.get`: Gives access to `/hcl get`.
- `hardcorelives.set`: Gives access to `/hcl set`.
- `hardcorelives.revive`: Gives access to `/hcl revive`.

### Placeholders

When using the PlaceholderAPI in conjunction with this plugin you will have the following placeholders at your disposal.

- `%hardcorelives_lives%`: Player lives left.
- `%hardcorelives_lives_hearts%`: Player lives left multiplied by 2 to show full heartColor for each life.
- `%hardcorelives_starting_lives%`: Players default starting number of lives. These are the `lives` defined in
  the `config.yml`

### Language

The project can handle different locales, you can change the locale on the `config.yml` property `locale`.

To add a specific translation or override one you should add the messages properties with the corresponding locale.
<br>For example, for locale `es-AR` you could either modify `messages_es.properties` or add `messages_es_AR.properties`.

### Action Bar

Since seeing your lives is an important part of the game you can opt to show the player lives in their action bar.

To do so you will need to add the custom resource pack to your server.
This custom resource pack uses custom font to display the hearts and uses negative spaces to move the action bar on top
of the hunger bar.

You can either show empty hearts or not show them. Not showing empty hearts is the best if people have manually set
different amount of lives.