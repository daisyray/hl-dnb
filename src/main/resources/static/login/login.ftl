<#import "/libs/utils.ftl" as libUtils>

<#assign pageJS>
    <script type='text/javascript'>
    function submitWithAction(action) {
        const form = document.getElementById("loginForm");
        form.action = action;
        form.submit();
    }
    function forgot() {
        submitWithAction('/user/forgot-password');
    }
    function registerSubmit() {
        submitWithAction('/register');
    }
    </script>
</#assign>

<@layout.layout showHeader=false showFooter=false page_js=pageJS>
    <div class="valign-wrapper" style="height: 80vh">
        <div style='width: 50%; margin: auto;'>
            <form id='loginForm' method="post" enctype="application/x-www-form-urlencoded" action="/user/login">
                <div class="input-field" style='margin-bottom: 40px;'>
                    <input type="email" id="email" name="email" value="${email!""}" class='validate'/>
                    <label for="email">Email</label>
                    <@libUtils.showError "email"/>
                </div>
                <div class="input-field">
                    <input type="password" id="password" name="password" value="${password!""}"/>
                    <label for="password">Password</label>
                    <@libUtils.showError "password"/>
                </div>
                <div class='row' style='margin-top: 10px;'>
                    <a class='col s3 vcenter-content'
                        onclick='forgot()'
                        style='font-size: 0.9em; font-style: italic;'
                        href='#'>
                        Forgot Password?
                    </a>
                    <div class='col s7 offset-s5 right-content'>
                        <div style='margin-right:0.5em;font-size: 0.8em;font-style:italic' class='vcenter-content'>
                            <a href='/check-insurance'>Check Your Insurance</a>
                        </div>
                        <button type="submit" style='margin-right: 0.5em;' class="waves-effect waves-light btn"><i class="material-icons left">person</i>Login</button>
                        <button onclick='registerSubmit()' class="waves-effect waves-light btn" style='background-color:green;'><i class="material-icons left">person_add</i>Register</button>
                    </div>
                </div>
            </form>
            <#if recovery??>
                <div class='center-align' style='font-style: italic; font-size: 0.9em; margin-top: 3em;'>A Recovery Email has been sent to ${email!""}</div>
            </#if>
        </div>
    </div>
</@layout.layout>
