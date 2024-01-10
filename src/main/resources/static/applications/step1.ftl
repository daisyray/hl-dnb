<@layout.layout>
    <div class="valign-wrapper" style="height: 80vh">
        <div style='width: 50%; margin: auto;'>
            <form id='form' method="post" enctype="application/x-www-form-urlencoded" action="/applications/save-step1">
                <div class="input-field" style='margin-bottom: 40px;'>
                    <input id="first-name" name="first-name" value="${first-name!""}"/>
                    <label for="first-name">First Name</label>
                    <@libUtils.showError "email"/>
                </div>
            </form>
        </div>
    </div>
</@layout>