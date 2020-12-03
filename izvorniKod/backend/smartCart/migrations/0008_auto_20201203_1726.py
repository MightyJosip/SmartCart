# Generated by Django 3.1.2 on 2020-12-03 16:26

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('smartCart', '0007_auto_20201203_1717'),
    ]

    operations = [
        migrations.RenameField(
            model_name='opisartikla',
            old_name='barkod',
            new_name='sif_barkod',
        ),
        migrations.RenameField(
            model_name='opisartikla',
            old_name='trgovina',
            new_name='sif_trgovina',
        ),
        migrations.RenameField(
            model_name='opisartikla',
            old_name='vrsta',
            new_name='sif_vrsta',
        ),
        migrations.RenameField(
            model_name='opisartikla',
            old_name='zemlja',
            new_name='sif_zemlja',
        ),
        migrations.AddConstraint(
            model_name='opisartikla',
            constraint=models.UniqueConstraint(fields=('email', 'sif_barkod'), name='constraint_3'),
        ),
    ]
