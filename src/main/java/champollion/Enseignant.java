package champollion;

import java.util.ArrayList;

public class Enseignant extends Personne {

    private final ArrayList<ServicePrevu> servicesPrevusList = new ArrayList<>();
    private final ArrayList<Intervention> interventionsPlanifieesList = new ArrayList<>();

    public Enseignant(String nom, String email) {
        super(nom, email);
    }


    public int heuresPrevues() {
        // TODO: Implémenter cette méthode
        int somme = 0;
        for (ServicePrevu s : servicesPrevusList){
            somme = (int) Math.round(somme + (s.getVolumeTP()*0.75) + (s.getVolumeCM()*1.5) + s.getVolumeTD());
        }
        return somme;
    }


    public int heuresPrevuesPourUE(UE ue) {
        // TODO: Implémenter cette méthode

        int somme = 0;
        for (ServicePrevu s : servicesPrevusList){
            if (s.getUe() == ue){
                somme = (int) Math.round(somme + (s.getVolumeTP()*0.75) + (s.getVolumeCM()*1.5) + s.getVolumeTD());
            }
        }
        return somme;

    }

    
    public void ajouteEnseignement(UE ue, int volumeCM, int volumeTD, int volumeTP) {
        // TODO: Implémenter cette méthode
        servicesPrevusList.add(new ServicePrevu(volumeCM,volumeTD,volumeTP,ue,this));
    }

    public void ajouteIntervention(Intervention inter) throws Exception {
        if(inter.getIntervenant() == this){
            interventionsPlanifieesList.add(inter);
        }else{
            throw new Exception("Cette intervention ne correspond pas à cet enseignant");
        }
    }

    public boolean enSousService() throws Exception {
        int nbHeuresPrevues = heuresPrevues();
        double sumNbHeuresRealisees = sommeInterventionsPlanifiees();

        return (nbHeuresPrevues - sumNbHeuresRealisees) > 0;
    }


    public double sommeInterventionsPlanifiees() throws Exception {
        double sumNbHeuresRealisees = 0;

        for (Intervention inter : interventionsPlanifieesList){
            if (!inter.isAnnulee()) sumNbHeuresRealisees = sumNbHeuresRealisees + inter.dureeEquivalentTD();
        }
        return Math.round(sumNbHeuresRealisees);
    }


    public int resteAPlanifier(UE ue, TypeIntervention type) throws Exception {
        double sommeInter = 0;
        if (enSousService()){

            for(Intervention inter : interventionsPlanifieesList){
                if (inter.getType().equals(type) && inter.getMatiere().equals(ue)){
                    sommeInter = sommeInter + inter.dureeEquivalentTD();
                }
            }

            double sommeServ = 0;

            for (ServicePrevu servicePrevu : servicesPrevusList) {
                if (servicePrevu.getUe().equals(ue)) {
                    switch (type) {
                        case TP:
                            sommeServ += servicePrevu.getVolumeTP()*0.75;
                            break;
                        case TD:
                            sommeServ += servicePrevu.getVolumeTD();
                            break;
                        case CM:
                            sommeServ += servicePrevu.getVolumeCM()*1.5;
                            break;
                    }
                }
            }

            sommeInter -= Math.round(sommeServ);
        }
        return (int) Math.abs(Math.round(sommeInter));
    }

    public ArrayList<ServicePrevu> getServicesPrevusList() {
        return servicesPrevusList;
    }

    public ArrayList<Intervention> getInterventionsPlanifieesList() {
        return interventionsPlanifieesList;
    }
}
