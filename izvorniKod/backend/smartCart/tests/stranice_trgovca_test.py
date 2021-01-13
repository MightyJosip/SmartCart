import unittest
from selenium import webdriver
from selenium.webdriver.common.by import By


HOME_PAGE = 'http://localhost:8000'
BARCODE = '123456789'
CORRECT_USERNAME = 'ante@fer.hr'
CORRECT_PASSWORD = 'pwd'
NEW_USERNAME = 'new_user_test@smartestcart.com'
NEW_PASSWORD = 'smartcart is the best page ever'


class StraniceTrgovcaTest(unittest.TestCase):
    driver = None

    @classmethod
    def setUpClass(cls):
        cls.driver = webdriver.Firefox()

    def tearDown(self):
        self.driver.delete_all_cookies()

    @classmethod
    def tearDownClass(cls):
        cls.driver.close()

    def fill_login_form(self, username, password):
        self.driver.get(HOME_PAGE)
        self.driver.find_element(By.XPATH, '//button[text()="Log in"]').click()
        self.driver.find_element(By.NAME, "username").send_keys(username)
        self.driver.find_element(By.NAME, "password").send_keys(password)
        self.driver.find_element(By.NAME, "submit_button").click()

    def test_successful_login(self):
        self.fill_login_form("ante@fer.hr", "pwd")
        self.assertEqual(self.driver.current_url, f"{HOME_PAGE}/trgovac")

    def test_wrong_login(self):
        self.fill_login_form("ante@fer.hr", "abc")
        self.assertTrue(str(self.driver.current_url).startswith(f"{HOME_PAGE}/login"))

    def test_create_new_profile(self):
        self.driver.get(HOME_PAGE)
        self.driver.find_element(By.XPATH, '//button[text()="Sign up as kupac"]').click()
        self.driver.find_element(By.NAME, "email").send_keys(NEW_USERNAME)
        self.driver.find_element(By.NAME, "password").send_keys(NEW_PASSWORD)
        self.driver.find_element(By.NAME, "confirm_password").send_keys(NEW_PASSWORD)
        self.driver.find_element(By.NAME, "submit_button").click()
        self.fill_login_form(NEW_USERNAME, NEW_PASSWORD)
        self.assertTrue("Logged in as new_user_test@smartestcart.com" in self.driver.page_source)

    def test_adding_new_barcode(self):
        self.fill_login_form("ante@fer.hr", "pwd")
        self.driver.find_element(By.NAME, "barkod_artikla").send_keys(BARCODE)
        self.driver.find_element(By.NAME, "new_barcode_button").click()
        list_of_products = self.driver.find_element(By.ID, "products")
        self.assertTrue(BARCODE in list_of_products.text)

    def test_wrong_link(self):
        self.driver.get(f"{HOME_PAGE}/another_link/that_doesnt_exist")
        self.assertEqual(self.driver.current_url, f"{HOME_PAGE}/")

    def test_page_permissions(self):
        self.fill_login_form(NEW_USERNAME, NEW_PASSWORD)
        self.driver.get(f"{HOME_PAGE}/trgovac")
        self.assertEqual(self.driver.current_url, f"{HOME_PAGE}/")


if __name__ == '__main__':
    unittest.main()
