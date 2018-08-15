<img src="icon.png" align="right" width="180px"/>

# ZeroPoint


[>> Downloads <<](https://github.com/b0undarybreaker/ZeroPoint/releases)

*Energy for a new age!*

**This mod is open source and under a permissive license.** All Elytra mods are,
and as such, can be included in any modpack on any platform without prior
permission. We appreciate hearing about people using our mods, but you do not
need to ask to use them. See the [LICENSE file](LICENSE) and [API LICENSE](src/main/java/com/elytradev/infraredstone/api/LICENSE_API)for more details.

ZeroPoint is an energy system for Rift and Minecraft 1.13. It is mainly an API and adds a generator as an example block. There are a few fundamental goals:

- **Provide a deflated environment**. RF and Forge Energy went through a massive power creep, to the point where base-level cables transferred thousands of RF/t and base-level batteries stored millions. This isn't conducive to a fun experience, so ZeroPoint is starting fresh.
- **Stay simple and easy to understand**. ZeroPoint exposes itself in energy per second instead of per tick, so you have a better sense of how long things really take. Ticks are great for computers to understand, but not as great for humans.
