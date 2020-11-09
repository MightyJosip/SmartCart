from django.test import TestCase, Client
import unittest

# Create your tests here.

class ServerTest(TestCase):
    def setUp(self):
        self.client = Client()
    
    # testne funkcije trebaju započeti s "test"
    def test_index(self):
        response = self.client.get('/')
        self.assertEqual(response.status_code, 200)

class Androidtest(TestCase):
    def setUp(self):
        self.client = Client()

    def test_trgovine(self):
        response = self.client.get('/')
        print(response)