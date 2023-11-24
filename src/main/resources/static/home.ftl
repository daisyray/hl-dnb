<@layout.layout>
    <div class='valign-wrapper' style='height: 80vh;'>
        <div style="display: flex; flex-direction: column; justify-content: flex-start; margin: auto;">
            <#assign centerJustify>display: flex; flex-direction: row; justify-content: center; width: 100%;</#assign>
            <div style='${centerJustify}'>
                <@oneCard "Policies" "/policies"/>
                <@oneCard "Applications" "/applications"/>
                <@oneCard "Payments" "/payments"/>
            </div>
            <div style='${centerJustify}'>
                <@oneCard "Reports" "/reports"/>
                <@oneCard "User Profile" "/profile"/>
                <@oneCard "Customer Support" "/customer-support"/>
            </div>
        </div>
    </div>
</@layout.layout>

<#macro oneCard title="N/A" link="">
    <div class="card blue-grey darken-1" style='margin: 1em; width: 300px;'>
        <div class="card-content white-text">
          <a href='${link}'>
          <span class="card-title" style='color: #ffab40;'>${title}</span>
          <p style='color: white;'>I am a very simple card. I am good at containing small bits of information.
          I am convenient because I require little markup to use effectively.</p>
          </a>
        </div>
    </div>
</#macro>
