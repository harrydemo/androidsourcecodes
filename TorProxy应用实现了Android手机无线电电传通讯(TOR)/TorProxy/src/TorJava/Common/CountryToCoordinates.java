/**
 * OnionCoffee - Anonymous Communication through TOR Network
 * Copyright (C) 2005-2007 RWTH Aachen University, Informatik IV
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */
package TorJava.Common;

import java.util.Set;
import java.util.HashMap;

public class CountryToCoordinates {
	static HashMap<String, Coordinates> map;
	
	public CountryToCoordinates() {
		init();
	}

	private static void init() {
		if (map != null) return;
		map = new HashMap<String, Coordinates>(300);
		// countries which are not covered with this list should be returned as somewhere
		// in the US...
    //map.put("A2",new Coordinates(37,-94)); // SatelliteProvider  (somewhere in the US)
    map.put("AD",new Coordinates(42.5,1.5)); // Andorra
    map.put("AE",new Coordinates(24,54)); // UnitedArabEmirates
    map.put("AF",new Coordinates(34,66)); // Afghanistan
    map.put("AG",new Coordinates(17.5,-61.75)); // AntiguaandBarbuda
    map.put("AI",new Coordinates(18.25,-63)); // Anguilla
    map.put("AL",new Coordinates(41,20)); // Albania
    map.put("AM",new Coordinates(40,45)); // Armenia
    map.put("AN",new Coordinates(12,-69)); // NetherlandsAntilles
    map.put("AO",new Coordinates(-13,18)); // Angola
    map.put("AP",new Coordinates(0,0)); // Asia/PacificRegion
    map.put("AQ",new Coordinates(0,0)); // Antarctica
    map.put("AR",new Coordinates(-34,-64)); // Argentina
    map.put("AS",new Coordinates(0,0)); // AmericanSamoa
    map.put("AT",new Coordinates(47.5,15)); // Austria
    map.put("AU",new Coordinates(-24,134)); // Australia
    map.put("AW",new Coordinates(0,0)); // Aruba
    map.put("AZ",new Coordinates(0,0)); // Azerbaijan
    map.put("BA",new Coordinates(0,0)); // BosniaandHerzegovina
    map.put("BB",new Coordinates(0,0)); // Barbados
    map.put("BD",new Coordinates(0,0)); // Bangladesh
    map.put("BE",new Coordinates(50.5,5)); // Belgium
    map.put("BF",new Coordinates(0,0)); // BurkinaFaso
    map.put("BG",new Coordinates(42.5,24)); // Bulgaria
    map.put("BH",new Coordinates(0,0)); // Bahrain
    map.put("BI",new Coordinates(0,0)); // Burundi
    map.put("BJ",new Coordinates(0,0)); // Benin
    map.put("BM",new Coordinates(0,0)); // Bermuda
    map.put("BN",new Coordinates(0,0)); // BruneiDarussalam
    map.put("BO",new Coordinates(-17,-65)); // Bolivia
    map.put("BR",new Coordinates(-10,-52)); // Brazil
    map.put("BS",new Coordinates(0,0)); // Bahamas
    map.put("BT",new Coordinates(0,0)); // Bhutan
    map.put("BV",new Coordinates(0,0)); // BouvetIsland
    map.put("BW",new Coordinates(0,0)); // Botswana
    map.put("BY",new Coordinates(54,28)); // Belarus
    map.put("BZ",new Coordinates(0,0)); // Belize
    map.put("CA",new Coordinates(51,-100)); // Canada
    map.put("CD",new Coordinates(0,0)); // Congo
    map.put("CF",new Coordinates(0,0)); // CentralAfricanRepublic
    map.put("CG",new Coordinates(0,0)); // Congo
    map.put("CH",new Coordinates(47,8)); // Switzerland
    map.put("CI",new Coordinates(0,0)); // CoteD'Ivoire
    map.put("CK",new Coordinates(0,0)); // CookIslands
    map.put("CL",new Coordinates(-34,-71)); // Chile
    map.put("CM",new Coordinates(0,0)); // Cameroon
    map.put("CN",new Coordinates(34,100)); // China
    map.put("CO",new Coordinates(4,-73)); // Colombia
    map.put("CR",new Coordinates(10,-84)); // CostaRica
    map.put("CS",new Coordinates(0,0)); // SerbiaandMontenegro
    map.put("CU",new Coordinates(23,-82)); // Cuba
    map.put("CV",new Coordinates(0,0)); // CapeVerde
    map.put("CY",new Coordinates(35,33)); // Cyprus
    map.put("CZ",new Coordinates(50,15)); // CzechRepublic
    map.put("DE",new Coordinates(51,10)); // Germany
    map.put("DJ",new Coordinates(0,0)); // Djibouti
    map.put("DK",new Coordinates(56,10)); // Denmark
    map.put("DM",new Coordinates(0,0)); // Dominica
    map.put("DO",new Coordinates(0,0)); // DominicanRepublic
    map.put("DZ",new Coordinates(0,0)); // Algeria
    map.put("EC",new Coordinates(0,0)); // Ecuador
    map.put("EE",new Coordinates(59,26)); // Estonia
    map.put("EG",new Coordinates(26,29)); // Egypt
    map.put("ER",new Coordinates(0,0)); // Eritrea
    map.put("ES",new Coordinates(40,4)); // Spain
    map.put("ET",new Coordinates(0,0)); // Ethiopia
    map.put("EU",new Coordinates(47,11)); // Europe
    map.put("FI",new Coordinates(62,25)); // Finland
    map.put("FJ",new Coordinates(0,0)); // Fiji
    map.put("FK",new Coordinates(0,0)); // FalklandIslands(Malvinas)
    map.put("FM",new Coordinates(0,0)); // Micronesia
    map.put("FO",new Coordinates(0,0)); // FaroeIslands
    map.put("FR",new Coordinates(46,2)); // France
    map.put("GA",new Coordinates(0,0)); // Gabon
    map.put("GB",new Coordinates(52,0)); // UnitedKingdom
    map.put("GD",new Coordinates(0,0)); // Grenada
    map.put("GE",new Coordinates(0,0)); // Georgia
    map.put("GF",new Coordinates(0,0)); // FrenchGuiana
    map.put("GH",new Coordinates(0,0)); // Ghana
    map.put("GI",new Coordinates(0,0)); // Gibraltar
    map.put("GL",new Coordinates(0,0)); // Greenland
    map.put("GM",new Coordinates(0,0)); // Gambia
    map.put("GN",new Coordinates(0,0)); // Guinea
    map.put("GP",new Coordinates(0,0)); // Guadeloupe
    map.put("GQ",new Coordinates(0,0)); // EquatorialGuinea
    map.put("GR",new Coordinates(39,22)); // Greece
    map.put("GT",new Coordinates(0,0)); // Guatemala
    map.put("GU",new Coordinates(0,0)); // Guam
    map.put("GW",new Coordinates(0,0)); // Guinea-Bissau
    map.put("GY",new Coordinates(0,0)); // Guyana
    map.put("HK",new Coordinates(22.25,114.16)); // HongKong
    map.put("HM",new Coordinates(0,0)); // HeardIslandandMcDonaldIslands
    map.put("HN",new Coordinates(0,0)); // Honduras
    map.put("HR",new Coordinates(46,16)); // Croatia
    map.put("HT",new Coordinates(0,0)); // Haiti
    map.put("HU",new Coordinates(47,20)); // Hungary
    map.put("ID",new Coordinates(0,113)); // Indonesia
    map.put("IE",new Coordinates(53,-8)); // Ireland
    map.put("IL",new Coordinates(31.75,35)); // Israel
    map.put("IN",new Coordinates(21,78)); // India
    map.put("IO",new Coordinates(0,0)); // BritishIndianOceanTerritory
    map.put("IQ",new Coordinates(32,44)); // Iraq
    map.put("IR",new Coordinates(35,52)); // Iran
    map.put("IS",new Coordinates(65,-18)); // Iceland
    map.put("IT",new Coordinates(42,13)); // Italy
    map.put("JM",new Coordinates(0,0)); // Jamaica
    map.put("JO",new Coordinates(0,0)); // Jordan
    map.put("JP",new Coordinates(36,139)); // Japan
    map.put("KE",new Coordinates(0,0)); // Kenya
    map.put("KG",new Coordinates(0,0)); // Kyrgyzstan
    map.put("KH",new Coordinates(0,0)); // Cambodia
    map.put("KI",new Coordinates(0,0)); // Kiribati
    map.put("KM",new Coordinates(0,0)); // Comoros
    map.put("KN",new Coordinates(0,0)); // SaintKittsandNevis
    map.put("KP",new Coordinates(40,126)); // Korea
    map.put("KR",new Coordinates(36,127)); // Korea
    map.put("KW",new Coordinates(0,0)); // Kuwait
    map.put("KY",new Coordinates(0,0)); // CaymanIslands
    map.put("KZ",new Coordinates(0,0)); // Kazakstan
    map.put("LA",new Coordinates(18,102)); // LaoPeople'sDemocraticRepublic
    map.put("LB",new Coordinates(0,0)); // Lebanon
    map.put("LC",new Coordinates(0,0)); // SaintLucia
    map.put("LI",new Coordinates(47.1,9.5)); // Liechtenstein
    map.put("LK",new Coordinates(0,0)); // SriLanka
    map.put("LR",new Coordinates(0,0)); // Liberia
    map.put("LS",new Coordinates(0,0)); // Lesotho
    map.put("LT",new Coordinates(55,23)); // Lithuania
    map.put("LU",new Coordinates(49.75,6)); // Luxembourg
    map.put("LV",new Coordinates(57,25)); // Latvia
    map.put("LY",new Coordinates(0,0)); // LibyanArabJamahiriya
    map.put("MA",new Coordinates(0,0)); // Morocco
    map.put("MC",new Coordinates(0,0)); // Monaco
    map.put("MD",new Coordinates(0,0)); // Moldova
    map.put("MG",new Coordinates(0,0)); // Madagascar
    map.put("MH",new Coordinates(0,0)); // MarshallIslands
    map.put("MK",new Coordinates(0,0)); // Macedonia
    map.put("ML",new Coordinates(0,0)); // Mali
    map.put("MM",new Coordinates(0,0)); // Myanmar
    map.put("MN",new Coordinates(0,0)); // Mongolia
    map.put("MO",new Coordinates(0,0)); // Macau
    map.put("MP",new Coordinates(0,0)); // NorthernMarianaIslands
    map.put("MQ",new Coordinates(0,0)); // Martinique
    map.put("MR",new Coordinates(0,0)); // Mauritania
    map.put("MS",new Coordinates(0,0)); // Montserrat
    map.put("MT",new Coordinates(0,0)); // Malta
    map.put("MU",new Coordinates(0,0)); // Mauritius
    map.put("MV",new Coordinates(0,0)); // Maldives
    map.put("MW",new Coordinates(0,0)); // Malawi
    map.put("MX",new Coordinates(25,-101)); // Mexico
    map.put("MY",new Coordinates(4,102)); // Malaysia
    map.put("MZ",new Coordinates(0,0)); // Mozambique
    map.put("NA",new Coordinates(0,0)); // Namibia
    map.put("NC",new Coordinates(0,0)); // NewCaledonia
    map.put("NE",new Coordinates(0,0)); // Niger
    map.put("NF",new Coordinates(0,0)); // NorfolkIsland
    map.put("NG",new Coordinates(0,0)); // Nigeria
    map.put("NI",new Coordinates(0,0)); // Nicaragua
    map.put("NL",new Coordinates(52.5,6)); // Netherlands
    map.put("NO",new Coordinates(61,8)); // Norway
    map.put("NP",new Coordinates(0,0)); // Nepal
    map.put("NR",new Coordinates(0,0)); // Nauru
    map.put("NU",new Coordinates(0,0)); // Niue
    map.put("NZ",new Coordinates(-42,172)); // NewZealand
    map.put("OM",new Coordinates(0,0)); // Oman
    map.put("PA",new Coordinates(0,0)); // Panama
    map.put("PE",new Coordinates(-12,-77)); // Peru
    map.put("PF",new Coordinates(0,0)); // FrenchPolynesia
    map.put("PG",new Coordinates(0,0)); // PapuaNewGuinea
    map.put("PH",new Coordinates(16,121)); // Philippines
    map.put("PK",new Coordinates(0,0)); // Pakistan
    map.put("PL",new Coordinates(52,20)); // Poland
    map.put("PR",new Coordinates(0,0)); // PuertoRico
    map.put("PS",new Coordinates(0,0)); // PalestinianTerritory
    map.put("PT",new Coordinates(39,-8)); // Portugal
    map.put("PW",new Coordinates(0,0)); // Palau
    map.put("PY",new Coordinates(0,0)); // Paraguay
    map.put("QA",new Coordinates(0,0)); // Qatar
    map.put("RE",new Coordinates(0,0)); // Reunion
    map.put("RO",new Coordinates(45,24)); // Romania
    map.put("RU",new Coordinates(56,38)); // RussianFederation
    map.put("RW",new Coordinates(0,0)); // Rwanda
    map.put("SA",new Coordinates(24,44)); // SaudiArabia
    map.put("SB",new Coordinates(0,0)); // SolomonIslands
    map.put("SC",new Coordinates(0,0)); // Seychelles
    map.put("SD",new Coordinates(0,0)); // Sudan
    map.put("SE",new Coordinates(62,15)); // Sweden
    map.put("SG",new Coordinates(1.3,103.8)); // Singapore
    map.put("SI",new Coordinates(0,0)); // Slovenia
    map.put("SK",new Coordinates(49,19)); // Slovakia
    map.put("SL",new Coordinates(0,0)); // SierraLeone
    map.put("SM",new Coordinates(0,0)); // SanMarino
    map.put("SN",new Coordinates(0,0)); // Senegal
    map.put("SO",new Coordinates(0,0)); // Somalia
    map.put("SR",new Coordinates(0,0)); // Suriname
    map.put("ST",new Coordinates(0,0)); // SaoTomeandPrincipe
    map.put("SV",new Coordinates(0,0)); // ElSalvador
    map.put("SY",new Coordinates(0,0)); // SyrianArabRepublic
    map.put("SZ",new Coordinates(0,0)); // Swaziland
    map.put("TC",new Coordinates(0,0)); // TurksandCaicosIslands
    map.put("TD",new Coordinates(0,0)); // Chad
    map.put("TF",new Coordinates(0,0)); // FrenchSouthernTerritories
    map.put("TG",new Coordinates(0,0)); // Togo
    map.put("TH",new Coordinates(15,101)); // Thailand
    map.put("TJ",new Coordinates(0,0)); // Tajikistan
    map.put("TK",new Coordinates(0,0)); // Tokelau
    map.put("TM",new Coordinates(0,0)); // Turkmenistan
    map.put("TN",new Coordinates(0,0)); // Tunisia
    map.put("TO",new Coordinates(0,0)); // Tonga
    map.put("TR",new Coordinates(39,35)); // Turkey
    map.put("TT",new Coordinates(0,0)); // TrinidadandTobago
    map.put("TV",new Coordinates(0,0)); // Tuvalu
    map.put("TW",new Coordinates(24,121)); // Taiwan
    map.put("TZ",new Coordinates(0,0)); // Tanzania
    map.put("UA",new Coordinates(45,31)); // Ukraine
    map.put("UG",new Coordinates(0,0)); // Uganda
    map.put("UM",new Coordinates(0,0)); // UnitedStatesMinorOutlyingIslands
    map.put("US",new Coordinates(37,-94)); // UnitedStates
    map.put("UY",new Coordinates(-33,-56)); // Uruguay
    map.put("UZ",new Coordinates(0,0)); // Uzbekistan
    map.put("VA",new Coordinates(0,0)); // HolySee(VaticanCityState)
    map.put("VC",new Coordinates(0,0)); // SaintVincentandtheGrenadines
    map.put("VE",new Coordinates(8,-65)); // Venezuela
    map.put("VG",new Coordinates(0,0)); // VirginIslands
    map.put("VI",new Coordinates(0,0)); // VirginIslands
    map.put("VN",new Coordinates(0,0)); // Vietnam
    map.put("VU",new Coordinates(0,0)); // Vanuatu
    map.put("WF",new Coordinates(0,0)); // WallisandFutuna
    map.put("WS",new Coordinates(0,0)); // Samoa
    map.put("YE",new Coordinates(0,0)); // Yemen
    map.put("YT",new Coordinates(0,0)); // Mayotte
    map.put("ZA",new Coordinates(-31,22)); // SouthAfrica
    map.put("ZM",new Coordinates(0,0)); // Zambia
    map.put("ZW",new Coordinates(0,0)); // Zimbabwe
	}

	public static double getX(String country) {
		init();
		if (map.containsKey(country)) {
			Coordinates c = (Coordinates) map.get(country);
			if ((c.north != 0) || (c.east !=0 )) return c.north;
		};
		return getX("US");
	}
	public static double getY(String country) {
		init();
		if (map.containsKey(country)) {
			Coordinates c = (Coordinates) map.get(country);
			if ((c.north != 0) || (c.east !=0 )) return c.east;
		};
		return getY("US");
	}

	public static Set<String> getCountries() {
		init();
		return map.keySet();
	}
	
	private static class Coordinates {
		double north,east;
		Coordinates(double north,double east) {
			this.north = north;
			this.east = east;
		}
		Coordinates(int n,int e) {
			this.north = n;
			this.east = e;
		}
		Coordinates(double n,int e) {
			this.north = n;
			this.east = e;
		}
		Coordinates(int n,double e) {
			this.north = n;
			this.east = e;
		}
	}
}
