public class EmailValidator {
    public static final String PRINTABLE_CHAR = "!#$%&'*+-/=?^_`{|}~";
    public static final char DOT = '.';
    public static final char HYPHEN = '-';
    public static final char AT = '@';

    // Add your enumerated states after this line
    public class EmailValidator {

        private enum LocalPartState {
            LOCAL_START,
            LOCAL_PART,
            ONE_DOT,
            INVALID
        }

        private enum DomainState {
            DOMAIN_START,
            DOMAIN_LABEL,
            DOMAIN_HYPHEN,
            DOMAIN_DOT,
            INVALID
        }

        public static boolean isEmailValid(String address) {
            int atPos = email.indexOf('@');
            if (atPos < 1 || atPos != email.lastIndexOf('@')) {
                return false;
            }

            String localPart = email.substring(0, atPos);
            String domain = email.substring(atPos + 1);

            return validateLocalPart(localPart) && validateDomain(domain);
        }

        private static boolean validateLocalPart(String localPart) {
            if (localPart.length() == 0 || localPart.length() > 64) {
                return false;
            }

            LocalPartState state = LocalPartState.LOCAL_START;

            for (int i = 0; i < localPart.length(); i++) {
                char ch = localPart.charAt(i);

                switch (state) {
                    case LOCAL_START:
                        if (ch == '.' || !isLocalPartChar(ch)) {
                            return false;
                        }
                        state = LocalPartState.LOCAL_PART;
                        break;

                    case LOCAL_PART:
                        if (ch == '.') {
                            state = LocalPartState.ONE_DOT;
                        } else if (!isLocalPartChar(ch)) {
                            return false;
                        }
                        break;

                    case ONE_DOT:
                        if (ch == '.' || !isLocalPartChar(ch)) {
                            return false;
                        }
                        state = LocalPartState.LOCAL_PART;
                        break;

                    case INVALID:
                        return false;
                }
            }

            return state != LocalPartState.ONE_DOT; // Ensure it doesn't end on a dot
        }

        private static boolean validateDomain(String domain) {
            if (domain.length() == 0 || domain.length() > 253) {
                return false;
            }

            DomainState state = DomainState.DOMAIN_START;
            int labelLength = 0;
            int totalDomainCount = 0;

            for (int i = 0; i < domain.length(); i++) {
                char ch = domain.charAt(i);
                totalDomainCount++;

                if (totalDomainCount > 253) {
                    return false;
                }

                switch (state) {
                    case DOMAIN_START:
                        if (ch == '.' || ch == '-') {
                            return false;
                        } else if (isLetterOrDigit(ch)) {
                            state = DomainState.DOMAIN_LABEL;
                            labelLength = 1;
                        } else {
                            return false;
                        }
                        break;

                    case DOMAIN_LABEL:
                        if (ch == '.') {
                            if (labelLength == 0 || labelLength > 63) {
                                return false;
                            }
                            state = DomainState.DOMAIN_DOT;
                            labelLength = 0;
                        } else if (ch == '-') {
                            if (labelLength == 0) {
                                return false;
                            }
                            state = DomainState.DOMAIN_HYPHEN;
                            labelLength++;
                        } else if (isLetterOrDigit(ch)) {
                            labelLength++;
                            if (labelLength > 63) {
                                return false;
                            }
                        } else {
                            return false;
                        }
                        break;

                    case DOMAIN_HYPHEN:
                        if (ch == '.') {
                            return false;
                        } else if (isLetterOrDigit(ch)) {
                            state = DomainState.DOMAIN_LABEL;
                            labelLength++;
                            if (labelLength > 63) {
                                return false;
                            }
                        } else {
                            return false;
                        }
                        break;

                    case DOMAIN_DOT:
                        if (ch == '.' || ch == '-') {
                            return false;
                        } else if (isLetterOrDigit(ch)) {
                            state = DomainState.DOMAIN_LABEL;
                            labelLength = 1;
                        } else {
                            return false;
                        }
                        break;

                    case INVALID:
                        return false;
                }
            }

            return state != DomainState.DOMAIN_DOT && state != DomainState.DOMAIN_HYPHEN && labelLength > 0 && labelLength <= 63;
        }

        private static boolean isLocalPartChar(char ch) {
            if (isLetterOrDigit(ch)) return true;
            String specials = "!#$%&'*+-/=?^_`{|}~";
            return specials.indexOf(ch) >= 0;
        }

        private static boolean isLetterOrDigit(char ch) {
            return Character.isLetterOrDigit(ch);
        }

    }
}




