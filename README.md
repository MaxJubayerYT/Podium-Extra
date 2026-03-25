![icon](https://raw.githubusercontent.com/RaydanOMGr/Podium/refs/heads/master/fabric/src/main/resources/assets/podium/icon.png)

# Podium Extra  
> **Po**jav + So**dium** = **Podium**

A tiny Mixin mod that disables [Sodium](https://modrinth.com/mod/sodium)'s [PojavLauncher](https://github.com/PojavLauncherTeam/PojavLauncher) (and [AAMC Amethyst](https://github.com/AngelAuraMC/Amethyst-Android)) check when a compatible mobile renderer is present. **Requires Sodium to run.**
![if (isUsingPojavLauncher()) throw new RuntimeException("It appears that you are using PojavLauncher, which is not supported when " +                     "using Sodium. Please check your mods list.")](https://cdn.modrinth.com/data/fW8woQj4/images/cb8f648d44e0a2f57df66d3af61de2ea7d04f70e.png)

## Disclaimer
This doesn’t magically make [Sodium](https://github.com/CaffeineMC/sodium-fabric) work on all devices or renderers. You’re still on your own when it comes to compatibility issues.



## Runtime behavior
- Podium always strips Sodium mobile-launcher checks so Pojav/Amethyst can proceed past startup blocks.
- Podium masks launcher-identifying property/environment keys used by mobile checks and forces launcher-detection methods to return `false`.
- At startup Podium prints a safety notice with memory guidance (`M_jvm = R_total × φ`, with `φ = 0.30-0.40`) and a 720p internal scaling recommendation.
- Podium performs compatibility-class bytecode patching by method signature so it can keep working across older and newer Sodium builds even if class names move.

## Requirements
- **Minecraft:** 1.20+
- **Loader:** [Fabric](https://fabricmc.net/use/) 0.15+, [Quilt](https://quiltmc.org/), or [NeoForge](https://neoforged.net/)
- **Java:** 21 or newer
- **[Sodium](https://github.com/CaffeineMC/sodium-fabric):** 0.5.13 or newer
- **Launcher:** [PojavLauncher](https://github.com/PojavLauncherTeam/PojavLauncher) with MobileGlues or LTW


## Installation
1. Download the latest release of **Podium** from [Modrinth](https://modrinth.com/mod/podium) or [Curseforge](https://curseforge.com/minecraft/mc-mods/podium-sodium).
2. Make sure you have either [Fabric Loader](https://fabricmc.net/use/) or [Quilt Loader](https://quiltmc.org/) installed on your [PojavLauncher](https://github.com/PojavLauncherTeam/PojavLauncher) instance.
3. Put the `podium-1.1.0.jar` into your `mods` folder alongside [Sodium](https://github.com/CaffeineMC/sodium-fabric).
4. Use a supported renderer like **LTW** or **MobileGlues**.
5. Launch the game. If everything works, Sodium will no longer crash due to the Pojav check.


## What's the deal with Sodium and PojavLauncher?
[Sodium](https://github.com/CaffeineMC/sodium-fabric) replaces Minecraft’s rendering engine with something far more efficient, but that comes at a cost. [PojavLauncher](https://github.com/PojavLauncherTeam/PojavLauncher) is known to struggle with mods that mess with rendering in non-standard ways which is exactly what Sodium does.

These incompatibilities often led to crashes or visual glitches when running Sodium on Pojav. As a result, people (mostly kids) flooded the Sodium support channels asking for help with issues that weren’t actually Sodium’s fault. This got annoying quickly, so the Sodium team added a launcher check.

Originally, it was just a warning. As of Sodium 0.6.13 (or 0.5.13 on 1.20.1), it crashes the game.

JellySquid, the author of Sodium, has stated that the check for PojavLauncher will remain in the mod.


## So why does *this* mod exist?
Good question.

Yes, Sodium and Pojav have been historically incompatible, but that’s changing. Pojav relies on something called *renderers*, which act as a translation/compatibility layer between the game’s graphics code and mobile hardware. Old renderers were notoriously bad: either super slow or completely broken with anything beyond vanilla Minecraft.

But now we have **MobileGlues** and **LTW**, two newer renderers that massively improve both compatibility and performance:
- **MobileGlues**: Developed by [Swung0x48](https://github.com/Swung0x48) and a few others, offers solid compatibility and decent performance.
- **LTW**: Developed by [Artdell](https://github.com/artdeell), amazing performance and great compatibility with Sodium.

Thanks to these, running [Sodium](https://github.com/CaffeineMC/sodium-fabric) on [PojavLauncher](https://github.com/PojavLauncherTeam/PojavLauncher) is no longer guaranteed to cause crashes and visual glitches. Podium simply removes the crash mechanism.


## License
MIT. Do whatever you want.
