#!/usr/bin/env python
"""Django's command-line utility for administrative tasks."""
import os
import sys


def main():
    """Run administrative tasks."""
    os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'konfiguracija.settings')
    try:
        from django.core.management import execute_from_command_line
    except ImportError as exc:
        raise ImportError(
            "Couldn't import Django. Are you sure it's installed and "
            "available on your PYTHONPATH environment variable? Did you "
            "forget to activate a virtual environment?"
        ) from exc
    if "CREATE_DB" in os.environ and os.environ["CREATE_DB"] == "True":
        import create_database
    if "FILL_DB" in os.environ and os.environ["FILL_DB"] == "True":
        import fill_database
    execute_from_command_line(sys.argv)


if __name__ == '__main__':
    main()
