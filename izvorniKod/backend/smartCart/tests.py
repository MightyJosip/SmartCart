from django.test import TestCase, Client
import unittest

# Create your tests here.
# testne funkcije trebaju započeti s "test"

class ServerTest(TestCase):
    def setUp(self):
        self.client = Client()
    
    def test_index(self):
        response = self.client.get('/')
        self.assertEqual(response.status_code, 200)

    def test_login_for_logged_out_user(self):
        response = self.client.get('/login')
        self.assertEqual(response.status_code, 200)

    def test_login_for_logged_in_user(self):
        response = self.client.get('/logout')
        self.assertEqual(response.status_code, 200)

"""
    def test_signup(self):
        response = self.client.get('/signup')
        self.assertEqual(response.status_code, 200)

    def test_trgovac(self):
        response = self.client.get('/trgovac')
        self.assertEqual(response.status_code, 200)


class Androidtest(TestCase):
    def setUp(self):
        self.client = Client()

    def test_trgovine(self):
        response = self.client.get('/android/artikli')
        self.assertEqual(response.status_code, 200)
    
    def test_popis(self):
        response = self.client.get('/android/trgovine')
        self.assertEqual(response.status_code, 200)
    
    def test_login(self):
        response = self.client.get('/android/login')
        self.assertEqual(response.status_code, 200)

    def test_logout(self):
        response = self.client.get('/android/logout')
        self.assertEqual(response.status_code, 200)

    def test_signup(self):
        response = self.client.get('/android/signup')
        self.assertEqual(response.status_code, 200)
"""