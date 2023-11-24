class Layout {
    footerText: string;

    constructor(message?: string) {
        this.footerText = message ?? "ncr footer" ;
    }

    footer() {
        return this.footerText;
    }
}
