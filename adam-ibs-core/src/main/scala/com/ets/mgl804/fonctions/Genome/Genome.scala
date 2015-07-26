package com.ets.mgl804.fonctions.Genome

import com.ets.mgl804.persistance.ParquetLoader
import org.bdgenomics.formats.avro.{IbdIbdRelationType, Individual, PairwiseIbsIbd}
import org.slf4j.LoggerFactory

/**
 * Created by Ivan on 15-08-25 : ivan.kizema at gmail.com
 */
class Genome(filename:String) {
  private val listIndividuals = new ParquetLoader(filename)
  private var listPairwiseIbaIbd = scala.collection.mutable.Buffer[PairwiseIbsIbd]()
  private val logger = LoggerFactory.getLogger(this.getClass)

  def computeData() {
    val listIndividuals = this.listIndividuals.loadData()
    val nbIndividuals = listIndividuals.length
    for (individual1 <-0 to nbIndividuals-1) {
      for (individual2 <-individual1+1 to nbIndividuals-1) {
        val pairwiseIbsIbd = PairwiseIbsIbd.newBuilder()
          .setIndividual1(listIndividuals(individual1))
          .setIndividual2(listIndividuals(individual2))
          .setRelationType(getIbdIbdRelationType(listIndividuals(individual1),listIndividuals(individual2)))
          .setIbdExpectedValue(getIbdExpectedValue(listIndividuals(individual1),listIndividuals(individual2)))
          .build()
        this.listPairwiseIbaIbd.append(pairwiseIbsIbd)
      }
    }
    logger.info("Loaded PairwiseIbsIbd : "+this.listPairwiseIbaIbd.length)
  }

  def getIbdIbdRelationType(individual1:Individual, individual2:Individual) : IbdIbdRelationType = {
// Le champ RT est "calculé" en comparant les ID des familles (FID) des deux individus, les ID de
// leurs parents, vérifies si se sont des "founder" (c'est à dire que se sont des parents), et leurs
// ID individuels (IID). Cette comparaison va permettre de déterminer si les deux individus sont://
//
//    Si (a.FID != b.FID)
//      retourner "UN"
//    Si( !( a.founder || b.founder) )
//      Si (a.IDPAT == b.IDPAT && a.IDMAT == b.IDMAT)
//        retourner "FS"
//      else if(a.IDPAT == b.IDPAT || a.IDMAT == b.IDMAT)
//        retourner "HS"
//    Si ( a.IDPAT == b.IID || a.IDMAT == b.IID ||
//      b.IDPAT == a.IID || b.IDMAT == a.IID )
//      retourner "PO"
//    retourner "OT"
    if (individual1.getFamilyId != individual2.getFamilyId) {
      return IbdIbdRelationType.UN
    }
    else if (individual1.getPaternalId == individual1.getPaternalId &&
      individual1.getMaternalId == individual2.getMaternalId) {
      return IbdIbdRelationType.FS
    }
    else if (individual1.getPaternalId == individual1.getPaternalId ||
      individual1.getMaternalId == individual2.getMaternalId) {
      return IbdIbdRelationType.HS
    }
    else if (individual1.getPaternalId == individual2.getIndividualId ||
      individual1.getMaternalId == individual2.getIndividualId ||
      individual2.getPaternalId == individual1.getIndividualId ||
      individual2.getMaternalId == individual1.getIndividualId ) {
      return IbdIbdRelationType.PO
    }
    else {
      return IbdIbdRelationType.OT
    }
  }

  def getIbdExpectedValue(individual1:Individual, individual2:Individual): Double = {
    // Référence dans le code source Plink1.07: fichier cfamily.cpp, la fonction genrel() à la ligne 49
    var ibdExpectedValue = 0
    // Specific Cases
    if(individual1==individual2) {
      return 1
    }
    if(individual1.getFamilyId != individual2.getFamilyId) {
      return 0
    }
    if(individual1.getPaternalId!=individual2.getPaternalId &&
      individual1.getMaternalId!=individual2.getMaternalId &&
      individual1.getPaternalId!=individual2.getMaternalId &&
      individual1.getMaternalId!=individual2.getPaternalId ) {
      return 0
    }
    // ToDo : ibdExpectedValue calculation here

    return ibdExpectedValue
  }

  def viewContent() {
    logger.debug("Loaded PairwiseIbsIbd : "+this.listPairwiseIbaIbd.length)
//    this.listPairwiseIbaIbd.foreach(item => logger.debug(item.toString))
    this.listPairwiseIbaIbd.foreach(item => logger.debug("FID1 "+
      item.getIndividual1.getFamilyId+" IID1 "+item.getIndividual1.getIndividualId+
      " FID2 "+ item.getIndividual2.getFamilyId+" IID2 "+item.getIndividual2.getIndividualId +
      " RT "+item.getRelationType+" EZ "+item.getIbdExpectedValue))
  }
}
