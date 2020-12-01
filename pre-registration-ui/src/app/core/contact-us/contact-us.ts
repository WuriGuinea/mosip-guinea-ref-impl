

export interface ContactUsFormControlModal {
    name: string;
    email: string;
    reason: string;
    otherReason: string;
    objet: string,
    message: string;
}

export class ContactUs {

    // Dummy reasons
    static Reasons : object[] = [
        { key : "reason 1", value : "Raison 1"},
        { key : "reason 2", value : "Raison 2"},
        { key : "other", value : "Autre"}
    ];
}
