package com.ets.mgl804.fonctions.MakeBedPersistance

import java.io.File

import com.ets.mgl804.core.AppContext
import com.ets.mgl804.data._
import org.apache.avro.generic.IndexedRecord
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.bdgenomics.formats.avro._
import parquet.avro.{AvroParquetReader, AvroParquetWriter}

import scala.collection.JavaConverters._

/**
 * Created by ikizema on 15-07-03.
 */
class PersistImportRecord(importRecord: ImportRecord, filename: String) {
  //This is the path where Parquet files will be created
  private val originalPlinkRecord = importRecord
  private val DATA_PATH = "DATA/avro/"
  private val fileName = filename
  private val conf = AppContext.conf
  private val sc = AppContext.sc
  private val sqc = new SQLContext(sc)

  def main(args: Array[String]) {

  }

  def persistData() {
    val parquetFilePath = initialiseParqurFile()
    writeToFile(parquetFilePath)
  }

  def writeToFile(parquetFilePath:Path) {
    val parquetWriter = new AvroParquetWriter[IndexedRecord](parquetFilePath, Individual.getClassSchema())

    // Begin with BIM records (Calculated Variants)
    var variants = scala.collection.mutable.Buffer[Variant]()
    // Load to variants
    this.originalPlinkRecord.bimRecords.foreach(bimRecord => variants.append(createVariant(bimRecord)))

    // Begin with FAM records (Individuals)
    var individuals = scala.collection.mutable.Buffer[Individual]()
    // Load to individuals
    for (individualNum <-0 to this.originalPlinkRecord.famRecords.length-1) {
      individuals.append(createIndividual(this.originalPlinkRecord.famRecords(individualNum), variants, individualNum))
    }
    // Write to Avro
    individuals.foreach(individual => parquetWriter.write(individual))

    // Close parquetWriter
    parquetWriter.close()
  }

  def createVariant(bimRecord:BimRecord) : Variant = {
    var variant = new Variant()
    var contig = new Contig()
    contig.setContigName(bimRecord.getChronosome())
    variant.setContig(contig)
    variant.setStart(bimRecord.getBasepairPosition())
    variant.setEnd(bimRecord.getBasepairPosition())
    variant.setReferenceAllele(bimRecord.getMinorAllele())
    variant.setAlternateAllele(bimRecord.getMajorAllele())
//    System.out.println(variant.toString())
    return variant
  }

  // Load Individuals with Genotypes and associates Variants
  def createIndividual(famRecord:FamRecord, variants:scala.collection.mutable.Buffer[Variant], individualNum:Int) : Individual = {
    var individual = new Individual()
    individual.setFamilyId(famRecord.getIdFamily())
    individual.setIndividualId(famRecord.getIdIndividual())
    individual.setMaternalId(famRecord.getIdMaternal())
    individual.setPaternalId(famRecord.getIdPaternal())
    val sex = famRecord.getSexe();
    if (sex=="1") {
      individual.setSex(Sex.Male)
    } else if (sex=="2") {
      individual.setSex(Sex.Female)
    } else {
      individual.setSex(Sex.Unknown)
    }
    var phenotype = new Phenotype()
    phenotype.setPhenotype(famRecord.getPhenotype())
    individual.setPhenotype(phenotype)

    // Set Individual Genotype
    var individualGenotype = scala.collection.mutable.Buffer[Genotype]()

    // For now setting Base Alleles
    for (genotypeNum <- 0 to this.originalPlinkRecord.getSnpIndividual(individualNum).getLengh-1) {
      var genonype = new Genotype()
      val allelesBase = this.originalPlinkRecord.getSnpIndividual(individualNum).getSpnNumList(genotypeNum)
      genonype.setAllelesBase(allelesBase.asJava)   // Convertion Scala List to Java List
      individualGenotype.append(genonype)
    }
    individual.setGenotype(individualGenotype.toList.asJava)

    // For now setting Variants
    for (genotypeBasePairPosition <- 1 to individualGenotype.size ) {
      for (variantNum <-0 to variants.size-1) {
        if (variants(variantNum).getStart==genotypeBasePairPosition) {
          individualGenotype(genotypeBasePairPosition-1).setVariant(variants(variantNum))
        }
      }
    }
    individual.setGenotype(individualGenotype.toList.asJava)

    // For now setting alleles (Ref, Alt ...)
    for (genotypeNum <- 0 to individualGenotype.size - 1) {
      val allelesGenotypeAlleles = new Array[GenotypeAllele](2)
      for (i <- 0 to 1) {
        if (individualGenotype(genotypeNum).getAllelesBase.get(i)==Base.Error || individualGenotype(genotypeNum).getAllelesBase.get(i)==Base.Miss) {
          allelesGenotypeAlleles(i)=GenotypeAllele.NoCall
        } else if (individualGenotype(genotypeNum).getAllelesBase.get(i).toString==individualGenotype(genotypeNum).getVariant.getReferenceAllele) {
          allelesGenotypeAlleles(i)=GenotypeAllele.Ref
        } else if (individualGenotype(genotypeNum).getAllelesBase.get(i).toString==individualGenotype(genotypeNum).getVariant.getAlternateAllele) {
          allelesGenotypeAlleles(i)=GenotypeAllele.Alt
        } else {
          allelesGenotypeAlleles(i)=GenotypeAllele.OtherAlt
        }
      }
      individualGenotype(genotypeNum).setAlleles(List(allelesGenotypeAlleles(0),allelesGenotypeAlleles(1)).asJava)
    }
    individual.setGenotype(individualGenotype.toList.asJava)

    System.out.println(individual.toString())
    return individual;
  }

  def initialiseParqurFile() : Path= {
    //Define the Users and Messages parquet file paths
    val parquetFilePath:Path = new Path(DATA_PATH, this.fileName+".parquet")
    //We must make sure that the parquet file(s) are deleted because the following script doesn't replace the file.
    deleteIfExist(parquetFilePath.getName());
    return parquetFilePath;
  }

  /**
   * This function delete the file on the disk if it exist.
   */
  def deleteIfExist(fileName:String)
  {
    var fileTemp = new File(DATA_PATH+fileName);
    if (fileTemp.exists())
    {
      fileTemp.delete();
    }
  }

}
