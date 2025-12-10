public class GaraAstronavi {
    public static void main(String[] args) {

        System.out.println("GARA ASTRONAVI - Sistema di Gara");

        Giudice giudice = new Giudice(1000.0);

        giudice.mostraClassificaPrecedente();

        Astronave a1 = new Astronave("Apollo");
        a1.setVelocita(10.0);
        a1.setPunteggio(100.0);

        Astronave a2 = new Astronave("Hermes");
        a2.setVelocita(12.0);
        a2.setPunteggio(100.0);

        Astronave a3 = new Astronave("Orion");
        a3.setVelocita(8.0);
        a3.setPunteggio(100.0);

        Astronave a4 = new Astronave("Phoenix");
        a4.setVelocita(11.0);
        a4.setPunteggio(100.0);

        System.out.println("\nRegistrazione partecipanti:");
        giudice.aggiungiAstronave(a1);
        giudice.aggiungiAstronave(a2);
        giudice.aggiungiAstronave(a3);
        giudice.aggiungiAstronave(a4);

        System.out.println("✓ " + a1.getNome() + " - Velocità: " + a1.getVelocita() + " m/s");
        System.out.println("✓ " + a2.getNome() + " - Velocità: " + a2.getVelocita() + " m/s");
        System.out.println("✓ " + a3.getNome() + " - Velocità: " + a3.getVelocita() + " m/s");
        System.out.println("✓ " + a4.getNome() + " - Velocità: " + a4.getVelocita() + " m/s");

        Thread judgeThread = new Thread(giudice);
        Thread t1 = new Thread(a1);
        Thread t2 = new Thread(a2);
        Thread t3 = new Thread(a3);
        Thread t4 = new Thread(a4);

        System.out.println("\n INIZIO GARA! Obiettivo: " + giudice.getMetriTotali() + " metri\n");
        giudice.garaAvvio();

        judgeThread.start();
        t1.start();
        t2.start();
        t3.start();
        t4.start();

        try {
            Thread.sleep(2000);
            System.out.println("\nSimulazione anomalia grave su Phoenix!");
            a4.notificaAnomalia("Impatto meteorite massiccio", 30.0);
            giudice.fermaAstronave(a4);
            System.out.println("Phoenix fermata per ispezione!\n");

            Thread.sleep(1000);

            if (a4.getPunteggio() < 75) {
                System.out.println("Phoenix ritirata dalla gara per danni critici!");
                giudice.ritiraAstronave(a4);
                System.out.println("Partecipanti rimanenti: 3\n");
            } else {
                System.out.println("Phoenix riparte con velocità ridotta!\n");
                a4.setVelocita(6.0);
            }

            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        giudice.garaTermina();

        System.out.println("\n" + "═".repeat(50));
        giudice.mostraClassifica();
        System.out.println("═".repeat(50));

        System.out.println("\nStatistiche finali:");
        System.out.println("  • Metri totali percorso: " + giudice.getMetriTotali());
        System.out.println("  • Avanzamento cumulativo: " + String.format("%.2f", giudice.getMetriAvanzamento()) + " metri");

        System.out.println("\nSalvataggio classifica...");
        giudice.salvaClassifica();
        System.out.println("✓ Classifica salvata in classifica.txt");

        GestoreFile logFile = new GestoreFile("log_gara.txt");
        logFile.scrivi("_________________________________________");
        logFile.scrivi("Gara completata - " + java.time.LocalDateTime.now());
        logFile.scrivi("Partecipanti: 4 astronavi");
        logFile.scrivi("Distanza: " + giudice.getMetriTotali() + " metri");
        logFile.scrivi("_________________________________________");

        System.out.println("\nGara terminata con successo!");
    }
}
