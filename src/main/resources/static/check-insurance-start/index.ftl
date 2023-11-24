<#import "/libs/utils.ftl" as libUtils>
<#assign pageJS>
    <script type='text/javascript'>
    function onLoad() {
        const elems = document.querySelectorAll('.datepicker');
        const instances = M.Datepicker.init(elems, {
            format: 'mm/dd/yyyy'
        });

        const selects = document.querySelectorAll('select');
        M.FormSelect.init(selects, {});
    }
    document.addEventListener('DOMContentLoaded', onLoad);
    </script>
</#assign>

<@layout.layout showHeader=false showFooter=false page_js=pageJS>
    <div class='left-content' style='margin-top: 3em;'><a href='/user/login-screen'>Back</a></div>
    <div class='center-content' style='font-size: 2em; font-family: GillSans; font-style: italic; text-decoration: underline;'>
        <h4> Calculate Your Initial Estimate </h4>
    </div>
    <div class="valign-wrapper" style="height: 80vh">
        <div style='width: 50%; margin: auto; font-size: 1.5em;'>
            <form method='post' action='/check-insurance/calculate'>
                <div class='spacing-bottom-large'>
                    <div class='spacing-bottom'>You Are a:</div>
                    <div class='left-content'>
                        <label>
                            <input name="gender" type="radio" checked />
                            <span style='margin-right: 3em;'>Male</span>
                        </label>
                        <label>
                            <input name="gender" type="radio" />
                            <span>Female</span>
                        </label>
                    </div>
                </div>
                <div class='spacing-bottom-large'>
                    <div>Your Date of Birth:</div>
                    <div class="input-field" style='width: 15em;'><input type="text" name='dob' class="datepicker" value='1990-01-03' /></div>
                </div>
                <div class='spacing-bottom-large'>
                    <div class='spacing-bottom'>How would you rate your health?</div>
                    <div class='left-content'>
                        <label>
                            <input type='radio' name='health' value='excellent' checked/>
                            <span style='margin-right: 1em;'>Excellent</span>
                        </label>
                        <label>
                            <input type='radio' name='health' value='good'/>
                            <span style='margin-right: 1em;'>Good</span>
                        </label>
                        <label>
                            <input type='radio' name='health' value='fair'/>
                            <span style='margin-right: 1em;'>Fair</span>
                        </label>
                    </div>
                </div>
                <div class='spacing-bottom-large'>
                    <div class='spacing-bottom'>Do you use tobacco, e-cigarettes, or other nicotine-containing products?</div>
                    <div class='left-content'>
                        <label>
                            <input type='radio' name='tobacco' value='yes'/>
                            <span style='margin-right: 1em;'>Yes</span>
                        </label>
                        <label>
                            <input type='radio' name='tobacco' value='no' checked />
                            <span style='margin-right: 1em;'>No</span>
                        </label>
                    </div>
                </div>
                <div class='spacing-bottom-large'>
                    <div class='spacing-bottom'>Coverage Timeframe:</div>
                    <div class="input-field" style='width: 10em;'>
                        <select name='coverage-timeframe'>
                            <#list [5,10,15,20,25,30,35,40] as y>
                                <option value='${y}'>${y} Years</option>
                            </#list>
                        </select>
                    </div>
                </div>
                <div class='spacing-bottom-large'>
                    <div class='spacing-bottom'>Coverage Amount:</div>
                    <div class="input-field" style='width: 10em;'>
                        <select name='coverage-amount'>
                            <#list 1..8 as y>
                                <#assign amt=y*25000>
                                <option value='${amt?c}'>$${amt}</option>
                            </#list>
                            <#list 1..16 as y>
                                <#assign amt=(200+(y*50))*1000>
                                <option value='${amt?c}'>$${amt}</option>
                            </#list>
                        </select>
                    </div>
                </div>
                <div class='center-content'>
                    <button type="submit" class="waves-effect waves-light btn"><i class="material-icons left">check_circle</i>Calculate</button>
                </div>
            </form>
        </div>
    </div>
</@layout.layout>
