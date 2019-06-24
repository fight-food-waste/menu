package ffw_menu;

class ApiAuthException extends Exception {
    ApiAuthException() {
        System.out.println("Could not authenticate to the FFW API.");
    }
}
