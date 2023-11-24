var Layout = /** @class */ (function () {
    function Layout(message) {
        this.footerText = message !== null && message !== void 0 ? message : "ncr footer";
    }
    Layout.prototype.footer = function () {
        return this.footerText;
    };
    return Layout;
}());
