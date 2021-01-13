"""
Django settings for konfiguracija project.

Generated by 'django-admin startproject' using Django 3.1.2.

For more information on this file, see
https://docs.djangoproject.com/en/3.1/topics/settings/

For the full list of settings and their values, see
https://docs.djangoproject.com/en/3.1/ref/settings/
"""
import os
from pathlib import Path

CONST = {}
CONST_PATH = os.path.join(os.path.join(Path(os.path.dirname(__file__)).parent, "constants.txt"))
if os.path.exists(CONST_PATH):
    with open(CONST_PATH, 'r', encoding='utf-8') as file:
        file = file.readlines()
        for line in file:
            line = line.rstrip().split("=")
            CONST[line[0]] = line[1]
    
    DATABASES = {
        'default': {
            'ENGINE': 'django.db.backends.postgresql',
            'NAME': 'smartcart',
            'USER': CONST['DATABASE_USERNAME'],
            'PASSWORD': CONST['DATABASE_PASSWORD'],
            'HOST': CONST['DATABASE_IP'],
            'PORT': CONST['DATABASE_PORT'],
        }
    }
else:
    import dj_database_url # needed for heroku!
    CONST['SECRET_CODE'] = os.environ['SECRET_CODE']
    CONST['GOOGLE_CLIENT_SECRET'] = os.environ['GOOGLE_CLIENT_SECRET']
    DATABASES = {}
    DATABASES['default'] = dj_database_url.config(conn_max_age=600, ssl_require=True)

KEY = CONST['SECRET_CODE']

# Build paths inside the project like this: BASE_DIR / 'subdir'.
BASE_DIR = Path(__file__).resolve().parent.parent


# Quick-start development settings - unsuitable for production
# See https://docs.djangoproject.com/en/3.1/howto/deployment/checklist/

# SECURITY WARNING: keep the secret key used in production secret!
SECRET_KEY = KEY

# SECURITY WARNING: don't run with debug turned on in production!
DEBUG = False

#10.0.2.2 služi za spajanje emulatora iz android studia.
# Možete dodati lokalni ip s mobitela da možete s njega pokretati server, a pristupati s Windowsa
ALLOWED_HOSTS = ['10.0.2.2', 'localhost', '192.168.1.4', '192.168.0.24', '192.168.1.15',
    'preljevstoga-smartcart.herokuapp.com', '0.0.0.0']


# Application definition

INSTALLED_APPS = [
    'smartCart.apps.SmartCartConfig',
    'django.contrib.admin',
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',

    ###
    'django.contrib.sites',

    'allauth',
    'allauth.account',
    'allauth.socialaccount',

    
    'allauth.socialaccount.providers.google',


]

MIDDLEWARE = [
    'django.middleware.security.SecurityMiddleware',
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.middleware.common.CommonMiddleware',

    # 'django.middleware.csrf.CsrfViewMiddleware',

    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
    'django.middleware.clickjacking.XFrameOptionsMiddleware',
]

ROOT_URLCONF = 'konfiguracija.urls'

TEMPLATES = [
    {
        'BACKEND': 'django.template.backends.django.DjangoTemplates',
        'DIRS': [],
        'APP_DIRS': True,
        'OPTIONS': {
            'context_processors': [
                'django.template.context_processors.debug',
                'django.template.context_processors.request',
                'django.contrib.auth.context_processors.auth',
                'django.contrib.messages.context_processors.messages',
            ],
        },
    },
]

WSGI_APPLICATION = 'konfiguracija.wsgi.application'

# Password validation
# https://docs.djangoproject.com/en/3.1/ref/settings/#auth-password-validators

AUTH_PASSWORD_VALIDATORS = [
    {
        'NAME': 'django.contrib.auth.password_validation.UserAttributeSimilarityValidator',
    },
    {
        'NAME': 'django.contrib.auth.password_validation.MinimumLengthValidator',
    },
    {
        'NAME': 'django.contrib.auth.password_validation.CommonPasswordValidator',
    },
    {
        'NAME': 'django.contrib.auth.password_validation.NumericPasswordValidator',
    },
]

AUTH_USER_MODEL = 'smartCart.BaseUserModel'
ACCOUNT_USER_MODEL_USERNAME_FIELD = None
ACCOUNT_EMAIL_REQUIRED = True
ACCOUNT_USERNAME_REQUIRED = False
ACCOUNT_AUTHENTICATION_METHOD = 'email'
SOCIALACCOUNT_ADAPTER = 'smartCart.models.UserAccountAdapter'

# Internationalization
# https://docs.djangoproject.com/en/3.1/topics/i18n/

LANGUAGE_CODE = 'en-us'

TIME_ZONE = 'CET'

USE_I18N = True

USE_L10N = True

USE_TZ = True


# Static files (CSS, JavaScript, Images)
# https://docs.djangoproject.com/en/3.1/howto/static-files/

STATIC_URL = 'static/'
STATIC_ROOT = 'static/'


EMAIL_BACKEND = 'django.core.mail.backends.smtp.EmailBackend'
EMAIL_HOST = 'smtp.gmail.com'
EMAIL_HOST_USER = 'smartestcart@gmail.com'
EMAIL_HOST_PASSWORD = 'tsvdjfatqulglkwb' 
EMAIL_PORT = 587
EMAIL_USE_TLS = True
DEFAULT_FROM_EMAIL = 'default from email'


AUTHENTICATION_BACKENDS = [
    
    # Needed to login by username in Django admin, regardless of `allauth`
    'django.contrib.auth.backends.ModelBackend',

    # `allauth` specific authentication methods, such as login by e-mail
    'allauth.account.auth_backends.AuthenticationBackend',
    
]

SITE_ID = 1

# Provider specific settings
SOCIALACCOUNT_PROVIDERS = {
    'google': {
        # For each OAuth based provider, either add a ``SocialApp``
        # (``socialaccount`` app) containing the required client
        # credentials, or list them here:
        'APP': {
            'client_id': '197701493351-l5j9llbv8r93kce4ajgf1kque7bcqqhr.apps.googleusercontent.com',
            'secret': CONST['GOOGLE_CLIENT_SECRET'],
            'key': ''
        }
    }
}

LOGIN_REDIRECT_URL = 'index'

ACCOUNT_LOGOUT_ON_GET = True

# next section is needed for running create_database and fill_database scripts
# scripts_in_progress blocks running scripts again until env flags are changed
# that prevents crashing
import sys
sys.path.append(BASE_DIR)

creating_db = False
filling_db = False
siptxt = str(BASE_DIR/"smartCart"/"management"/"commands"/"scripts_in_progress.txt")
with open(siptxt, 'r') as inFile:
    data = inFile.read()
    creating_db = "creating_db" in data
    filling_db = "filling_db" in data
print("Starting again", creating_db, filling_db)
if "CREATE_DB" in os.environ and os.environ["CREATE_DB"] == "True":
    if not creating_db:
        with open(siptxt, 'a') as outFile:
            outFile.write("creating_db\n")
        import create_database
else:
    with open(siptxt, 'w') as outFile:
        outFile.write("filling_db\n" if filling_db else "")
    creating_db = False
if "FILL_DB" in os.environ and os.environ["FILL_DB"] == "True":
    if not filling_db:
        with open(siptxt, 'a') as outFile:
            outFile.write("filling_db\n")
        import fill_database
else:
    with open(siptxt, 'w') as outFile:
        outFile.write("creating_db\n" if creating_db else "")
    filling_db = False
