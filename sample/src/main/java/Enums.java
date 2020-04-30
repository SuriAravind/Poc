/**
 * Created by Suriyanarayanan K
 * on 23/04/20 10:50 AM.
 */

/**
 * Created by Suriyanarayanan K
 * on 22/04/20 8:15 PM.
 */
enum Country {
    India, UNKNOWN;

    public enum State {
        TamilNadu(India);
        Country country;

        State(Country country) {
            this.country = country;
        }

        public enum District {
            Thanjavur(India, TamilNadu);
            Country country;
            State state;

            District(Country country, State state) {
                this.country = country;
                this.state = state;
            }

            public Country getCountry() {
                return country;
            }
            public State getState() {
                return state;
            }
        }
    }
}

public class Enums {
    public static void main(String[] args) {

        System.out.println(Country.State.District.Thanjavur);
        System.out.println(Country.State.District.Thanjavur.getState());
        System.out.println(Country.State.District.Thanjavur.getCountry());

    }

}
