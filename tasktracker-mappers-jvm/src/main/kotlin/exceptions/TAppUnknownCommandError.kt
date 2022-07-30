package exceptions

import ru.ac1d.tasktracker.common.models.TAppCommand

class TAppUnknownCommandError(command: TAppCommand) : Throwable("Unknown command [$command] at mapping to transport model")