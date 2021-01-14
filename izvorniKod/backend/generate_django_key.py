from django.core.management import utils
with open("temp", 'w') as tempFile:
    tempFile.write(utils.get_random_secret_key())

