package exceptions

class TAppUnknownRequestError(clazz: Class<*>):
    Throwable("Request with type [$clazz] cannot be handled in application context")