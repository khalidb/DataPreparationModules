# Data preparation modules described using examples 

This repository contains data examples describing data preparation modules, which can be found in the folder data example.
A data example specify the name of the module, its provider, URI as well as the input and output. 
The inputs and outputs are described by a name and a semantic domain, as well as a value.

Here is the list of modules that we described using data examples:

* GenEntry (by DDBJ)
* FastaHeaderExtractor (by FABOX)
* FastaDatasetSplitter (by FABOX)
* Fasta2Excel (by FABOX)
* DNAcollapser (by FABOX)
* FastaToTCS (by FABOX)
* ARSA (by DDBJ)
* XQuest (by ETHZ)
* FindingExperiments (by EBI)
* SearchCHEBI (by the EBI)
* AssemblycDNA (by the EBI)
* AssemblyCDS (by the EBI)
* AssemblyMap (by the EBI)
* search_pathway (by Kegg)
* search_brite (by Kegg)
* search_disease (by Kegg)
* reconstruct_pathway (by Kegg)
* annotate_sequence (by Kegg)
* map_taxonomy (by kegg)
* GhostKOALA  (by Kegg)
* KofamKOALA (by Kegg)
* PREDIction of SIgnal peptides (by Predisi)
* Retrieve/ID (by Uniprot)
* PeptideSearch (by Uniprot)
* CutSeq (by Emboss)
* degapseq (by Emboss)
* entret (Emboss)
* trimset (by Emboss)
* union (by Emboss)
* vectorstrip (by Emboss)
* proteomecentral (by proteomecentral)
* GOlr (by Gene Ontology)
* GENEASE (by CCHMC)
* snp (by NCBI)
* Protein2GO (by QuickGO)
* GeneProducts (by QuickGO)

# Queries
We issues the following queries:
* Q1: What is the DNA sequence in an EBI format for a given DNA accession
* Q2: What are the pathways associated with a given gene term
* Q3: Format a Fasta biological sequence into a Uniprot format
* Q4: What is the GO term associated with a Protein Sequence
* Q5: What are the annotations associated with a biological sequence 

# Ground truth responses to the above queries
* Q1: getEntry (DDBJ)

# Performance of our algorithm for discovering data preparation modules
| query  | keyword search |Input and output based search |Amount of feedback on data examples |
| ------------- | ------------- |------------- |------------- |
| Q1  | 30 candidate modules  | 6  | 2  |
| Q2  | 8 candidate modules  | 3  | 1  |
| Q3  | 31 candidate modules  | 8  | 4  |
| Q4  | 27 candidate modules  | 4  | 2  |
| Q5  | 31 candidate modules  | 1  | 1  |


| Content Cell  | Content Cell  |Content Cell  |Content Cell  |

For query Q1, we were able to reduce dramatically the number of candidate modules from 6 to 1, thanks to the propagation of false positives between candidates. Initially, we have the following candidates: GetEntry, SearchCHEDI, ARSA, ASsembly, Seqret and DBFETCH. The false positives.
We did not have any module that perform the transformation requested by query Q3. 

