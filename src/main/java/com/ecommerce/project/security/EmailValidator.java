package com.ecommerce.project.security;

import jakarta.mail.internet.InternetAddress;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

public class EmailValidator {

    // Check if email format is valid
    public static boolean isValidEmailFormat(String email) {
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Check if domain has MX record
    public static boolean hasMxRecord(String email) {
        try {
            String domain = email.substring(email.indexOf("@") + 1);
            Hashtable<String, String> env = new Hashtable<>();
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            InitialDirContext idc = new InitialDirContext(env);
            Attributes attrs = idc.getAttributes(domain, new String[]{"MX"});
            return attrs != null && attrs.get("MX") != null;
        } catch (Exception e) {
            return false;
        }
    }

    // Combined check
    public static boolean isValidEmail(String email) {
        return isValidEmailFormat(email) && hasMxRecord(email);
    }
}
