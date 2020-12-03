from django.test import TestCase, Client
import unittest
from django.contrib.auth import get_user_model
User = get_user_model()
# Create your tests here.
# testne funkcije trebaju započeti s "test"

class ServerPageTest(TestCase):
    def setUp(self):
        self.client = Client()

    def test_index(self):
        response = self.client.get('/')
        self.assertEqual(response.status_code, 200)

class ServerLoginTest(TestCase):
    def setUp(self):
        self.client = Client()
        #self.userData = {'username': 'aante@feer.hr', 'password':'pwd'}
        #self.user = User.objects.create_user("aante@feer.hr", "pwd", isKupac=True)

    def test_login_page_for_logged_out_user(self):
        response = self.client.get('/login')
        self.assertEqual(response.status_code, 200)

    def test_login_page_for_logged_in_user(self):
        pass

    def test_logout_page_for_logged_out_user(self):
        response = self.client.get('/logout')
        self.assertEqual(response.status_code, 302)

    def test_logout_page_for_logged_in_user(self):
        pass

    def test_login_proces_for_logged_out_user(self): # :'(
        #response = self.client.post('/login', {'username': 'aante@feer.hr', 'password':'pwd'}, follow=True)
        #self.assertTrue(response.context['user'].is_authenticated)
        pass


class ServerSignupTest(TestCase):
    def setUp(self):
        self.client = Client()

class Androidtest(TestCase):
    def setUp(self):
        self.client = Client()

    def test_trgovine(self):
        response = self.client.post('/android/trgovine', {'barkodovi': '[1]'}, follow=True)
        print(response.content)
    
    def test_popis(self):
        pass
    
    def test_login(self):
        pass

    def test_logout(self):
        pass

    def test_signup(self):
        pass
