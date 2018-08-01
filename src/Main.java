public class Main {


    private static final String ENCRYPT = "ENCRYPT";
    private static final String DECRYPT = "DECRYPT";

        public static void main(String[] args) {

        }

        public static void processing(final String search, final String target) throws CustomException {

            if (target.equalsIgnoreCase(ENCRYPT)) {

            } else if (target.equalsIgnoreCase(DECRYPT)) {

            }else {
                throw new CustomException("No value found");
            }
        }
}
