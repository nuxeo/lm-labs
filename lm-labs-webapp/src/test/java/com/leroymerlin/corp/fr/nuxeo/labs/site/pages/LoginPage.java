/*
 * (C) Copyright 2006-2010 Nuxeo SAS (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     bstefanescu
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.pages;

import org.nuxeo.runtime.test.runner.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;


public class LoginPage extends WebPage {

    @FindBy(how=How.ID, using="username")
    protected WebElement inputUsername;

    @FindBy(how=How.ID, using="password")
    protected WebElement inputPassword;

    @FindBy(how=How.ID, using="login")
    protected WebElement login;

    @FindBy(how=How.ID, using="logout")
    protected WebElement logout;

    public void login(String username, String password) {
        inputUsername.clear();
        inputUsername.sendKeys(username);
        inputPassword.clear();
        inputPassword.sendKeys(password);
        login.click();
    }

    public void ensureLogin(String username, String password) {
        login(username, password);
        isAuthenticated(10);
    }

    public void logout() {
        logout.click();
    }

    public void ensureLogout() {
        logout();
        isNotAuthenticated(5);
    }

    public boolean isAuthenticated(int timeoutInSeconds) {
        return hasElement(By.id("logout"), timeoutInSeconds);
//        try {
//            findElement(By.id("logout"), timeoutInSeconds);
//            return true;
//        } catch (WebDriverException e) {
//            return false;
//        }
    }

    public boolean isNotAuthenticated(int timeoutInSeconds) {
        try {
            findElement(By.id("login"), timeoutInSeconds);
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    public boolean isAuthenticated() {
        try {
            driver.findElement(By.id("logout"));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

}
