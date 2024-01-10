<@layout.layout>
    <div class='valign-wrapper' style='height: 80vh;'>
        <div style="display: flex; flex-direction: column; justify-content: flex-start; margin: auto;">
            <form action="/register/new-password" method="post" enctype="application/x-www-form-urlencoded">
                Email: <input name='email' value="${email!""}"/>
                Password: <input name='password' value=""/>
                Confirm password: <input name='confirm-password' value=""/>
                <input type='submit'/>
            </form>
            </div>
        </div>
    </div>
</@layout.layout>




