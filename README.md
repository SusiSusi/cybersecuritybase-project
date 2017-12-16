# Cyber Security Base-project

This project is part of Cyber Security Base -course that is held by University of Helsinki in collaboration with F-Secure.
More info of the course can be found [here](https://cybersecuritybase.github.io/).

## Task:
Your task is to create a web application that has at least five different flaws from the 
[OWASP top ten list](https://www.owasp.org/index.php/Top_10_2013-Top_10). Starter code for the project is provided on Github at 
https://github.com/cybersecuritybase/cybersecuritybase-project. You will then write a brief (1000 words) report that outlines how 
the flaws can be first identified and then fixed.

### A6-Sensitive Data Exposure
Issue: A6-Sensitive Data Exposure. You can see previous user's personal data even though he/she has logged out.

Steps to reproduce:
1. Go to: localhost:8080
2. Enter Name and Address, for example name: David, address: something
3. Press Submit button
4. Now you have signed up to the event. Click link “See who else is coming!”
5. Now you can see, who else has registered for the event and you will also see your personal information. Address is information that other users may not see.
6. Press then link “Log out”.
7. Now you are in page localhost:8080/form
8. Change the URL to: localhost:8080/event
9. You can see all the information (including address) of the previous user

#### One way to fix it:
Go to SignupController (in the package: sec.project.controller) and write to the beginning of functions defaultMapping and loadForm: httpSession.invalidate(); 

    public String defaultMapping() {
        httpSession.invalidate();
        return "redirect:/form";
    }
    
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm() {
        httpSession.invalidate();
        return "form";
    }


### A7-Missing Function Level Access Control
Issue: A7-Missing Function Level Access Control. Only signed up -users can see the topsecret-page.

Step to  reproduce:
1. Add httpSession.invalidate(); to the SignupController's functions defaultMapping and loadForm  (just remove slash (//) marks before the httpSession.invalidate();)
2. Go to: localhost:8080/topsecret
3. You will see a message that is only intended for sign up -users

#### One way to fixed it:
Go to SignupController (in the package: sec.project.controller) and add to the function topSecret() code: 

        if (null == httpSession.getAttribute("name")) {
            return "redirect:/";
        }


### A8-Cross-Site Request Forgery (CSRF)
Issue: A8-Cross-Site Request Forgery (CSRF). Application has disable the CSRF-protection. This means that anyone can 
load content into user's browsers and force them to submit a request to this application.

#### How to fix it:
Go to SecurityConfiguration (in the package: sec.project.config) and remove from configure() code: http.csrf().disable();.

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().permitAll();
    }

### A3-Cross-Site Scripting (XSS)
Issue: A3-Cross-Site Scripting (XSS). A registered user can send messages which has a javascript code. When the next users go to see the messages, they will get a pop up -window for their browser.

Step to  reproduce:
1. Go to: localhost:8080
2. Enter Name and Address, for example name: David, address: something
3. Press Submit button
4. Now you have signed up to the event. Click link “See who else is coming!”
5. Add to the Message field: <script>alert("XSS attack!!");</script>
6. Press Submit button
7. Then, press “Log out”
8. You are now in localhost:8080/form
9. Enter Name and Address, for example name: Jon, address: address
10. Now you have signed up to the event. Click then link “See who else is coming!”. This causes the browser to run the previous added javascript code
10. You will get a pop up -window where reads: “XSS attack”

#### One way to fix it:
Go to event.html (in the path: src/main/resources and in the package: templates) and change the messages th:utex to th:tex:

        <ul>
            <li th:each="item : ${messageList}">
                <span th:text="${item.message}"></span>
            </li>
        </ul>

Another way is that you validate the address field so it can not take characters < and >.

### A9-Using Components with Known Vulnerabilities
Issue: A9-Using Components with Known Vulnerabilities. The application is using the old version of spring-boot-starter-parent. The application has its pom.xml above code which enables to analyze the used dependencies and identify components with vulnerabilities. The code below assumes that you have an older version of Maven (Maven 3.0). If you have at least Maven version 3.1 you can change the version to 1.4.4. You can check your Maven version by using command: `mvn -version`.

            <plugin>
                <groupId>org.owasp</groupId>
                <artifactId>dependency-check-maven</artifactId>
                <version>1.4.2</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>check</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

Step to  reproduce:
1. Go to the root of the application at the terminal
2. Run the command: `mvn dependency-check:check` (this may take a few minutes)
3. Now you can see how many vulnerabilities are in the application. You can find the vulnerabilities descriptions (for example CVE-2016-9878) using the Common Vulnerabilities and Exposures database at https://cve.mitre.org/index.html.

#### How to fix it:
We take one example which is spring-boot-starter-parent -version. The application uses the version 1.4.2.RELEASE. Change the spring-boot-starter-parent -version to the latest version 1.5.9.RELEASE. This doesn't fix all the vulnerabilities which were found with maven check. In fact, if you change the version to the latest and then run the command `mvn dependency-check:check`, you will get even more vulnerabilities in what was earlier. 

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.9.RELEASE</version>
    </parent>
