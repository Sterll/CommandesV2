package fr.sterll.skycraftskyblock.database;

public enum DatabaseManager {

    SkyCraftBDD(new DatabaseCredentials("188.40.238.67", "root", "S06092021", "skycraftskyblock", 3306));

    private DatabaseAccess databaseAccess;
    DatabaseManager(DatabaseCredentials credentials){
        this.databaseAccess = new DatabaseAccess(credentials);
    }

    public DatabaseAccess getDatabaseAccess(){
        return databaseAccess;
    }

    public static void initAllDatabaseConnection(){
        for(DatabaseManager databaseManager : values()){
            databaseManager.databaseAccess.initPool();
        }
    }
    public static void closeAllDatabaseConnections(){
        for(DatabaseManager databaseManager : values()){
            databaseManager.databaseAccess.closePool();
        }
    }

}
