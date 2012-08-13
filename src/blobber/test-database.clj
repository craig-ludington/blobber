(ns blobber.test-database
  (:require [blobber.config    :as config]
            [clojure.java.jdbc :as sql]))

(defn create-test-schema []
  (sql/with-connection (config/db :admin)
    (sql/create-table :blobs
                      [:id :serial "PRIMARY KEY"]
                      [:hash :varchar]
                      [:blob :text]
                      [:created_at :timestamp "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"])))

(defn create-test-data []
  (sql/with-connection (config/db :admin)
    (sql/insert-records
     :blobs
     {:blob "
Hugo Chavez corporate security kilderkin PET mindwar ISEC Croatian
crypto anarchy cybercash Khaddafi Sears Tower insurgency Operation
Iraqi Freedom SHA Kosovo
"}
     {:blob "
fraud BATF Ron Brown NASA TELINT Gazprom Rand Corporation Glock Etacs
Al Jazeera investigation Armani DES CDC ISEC
"}
     {:blob "
AIEWS target nitrate Bosnia KGB virus Ermes benelux MIT-LL SCUD
missile Lon Horiuchi cybercash NATO Roswell NSA
"}
     {:blob "
munitions CIA Vince Foster fissionable data haven MD2 EuroFed Agfa S
Key world domination government quarter subversive [Hello to all my
friends and fans in domestic surveillance] bootleg
"}
     {:blob "
unclassified Syria CID ASIO Leuken-Baden FIPS140 Osama Abbas Yukon
HAMASMOIS DRM Operation Iraqi Freedom Panama Defcon IMF
"}
     {:blob "
Uzbekistan AFSPC Commecen Mena Steve Case enemy of the state digicash
industrial espionage 64 Vauxhall Cross Glock militia JPL unclassified
Qaddafi Khaddafi
"}
     {:blob "
covert video bullion Majic world domination Cohiba Peking Attorney
General EuroFed explosion Becker morse Albania corporate security
Centro IRA
"}
     {:blob "
Kennedy JPL MD4 SHA USCODE Downing Street Manfurov $400 million in
gold bullion Craig Livingstone bomb high security BLU-97 A/B PGP SAFE
LABLINK
"}
     {:blob "
ARPA Qaddafi JFK Marxist CipherTAC-2000 number key USDOJ Compsec AVIP
Belknap csim security threat class struggle AK-47
"}
     {:blob "
CISU Albanian Manfurov freedom CBNRC Saudi Arabia Etacs BRLO Clinton
munitions Arnett Armani Medco PGP satellite imagery
"}
     {:blob "
quiche PLO Syria quarter assassination codes counter terrorism USCOI
9/11 enforcers Ermes Peking SSL Israel rail gun
"}
     {:blob "
Honduras White House IMF Mahmoud Ahmadinejad Consul AUTODIN gamma
INSCOM Noriega Osama Belknap ARPA Ft. Bragg pre-emptive asset
"}
     {:blob "
Belknap satellite imagery embassy threat USCODE Mossad M-14
interception TELINT MDA New World Order UFO Roswell virus Leuken-Baden
"}
     {:blob "
Security Council sniper UOP ASDIC BROMURE Taiwan Bellcore Uzbekistan
Firefly afsatcom Perl-RSA EuroFed Leuken-Baden Mole warfare
"}
     {:blob "
Firefly TWA Baranyi Geraldton Comirex lynch Roswell JFK AIEWS threat
NSA chameleon man Aladdin bce ANZUS
"}
     {:blob "
mania KGB NORAD Baranyi ANC argus Project Monarch Rumsfeld ASPIC
Mantis Taiwan BROMURE unclassified spies benelux
"}
     {:blob "
mania Arnett COSCO AIMSX Agfa high security INSCOM oil cryptanalysis
crypto anarchy Watergate SWAT MD5 MP5K-SD STARLAN
"}
     )))

(defn -main []
  (print "Creating test database ...") (flush)
  (create-test-schema)
  (create-test-data)
  (println " done"))
