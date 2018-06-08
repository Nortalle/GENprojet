-- Projet GEN - OTrain - Adrien Allemand, Kamil Amrani, Vincent Guidoux, Loyse Krug
-- Auteur: Loyse Krug

SET FOREIGN_KEY_CHECKS=0;

-- Destruction de la base de donnée si elle existe déjà
DROP SCHEMA IF EXISTS `GEN_otrain` ;

-- Création de la base 
CREATE SCHEMA IF NOT EXISTS `GEN_otrain` DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;

-- Les opérations suivantes seront faites dans la base OTrain
USE `GEN_otrain` ;

-- Table Utilisateur
CREATE TABLE IF NOT EXISTS `Utilisateur` (
  `nomUtilisateur` VARCHAR(45) NOT NULL,
  `motDePasse` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`nomUtilisateur`),
  UNIQUE INDEX `Utilisateur_UNIQUE` (`nomUtilisateur` ASC))
ENGINE = InnoDB;

-- table Admin
-- cette table permet simplement de créer un admin que ne peut pas être détruit
CREATE TABLE IF NOT EXISTS `Admin` (
  `nomAdmin` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`nomAdmin`),
  UNIQUE INDEX `admin_UNIQUE` (`nomAdmin` ASC),
  CONSTRAINT `fk_joueur_admin`
    FOREIGN KEY (`nomAdmin`)
    REFERENCES `Utilisateur` (`nomUtilisateur`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- table Joueur
CREATE TABLE IF NOT EXISTS `Joueur` (
  `nomJoueur` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`nomJoueur`),
  UNIQUE INDEX `joueur_UNIQUE` (`nomJoueur` ASC),
  CONSTRAINT `fk_joueur_utilisateur`
    FOREIGN KEY (`nomJoueur`)
    REFERENCES `Utilisateur` (`nomUtilisateur`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- table ObjetsParJoueurs
CREATE TABLE IF NOT EXISTS `ObjetsParJoueur` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nomJoueur` VARCHAR(45) NOT NULL,
  `objetId` INT NOT NULL DEFAULT 0,
  `objetAmount` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `ObjetsParJoueur_UNIQUE` (`id` ASC),
  CONSTRAINT `fk_joueur_objets`
    FOREIGN KEY (`nomJoueur`)
    REFERENCES `Joueur` (`nomJoueur`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- table Gare
CREATE TABLE IF NOT EXISTS `Gare` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `posX` INT NOT NULL,
  `posY` INT NOT NULL,
  `nbrQuai` INT NOT NULL,
  `tailleQuai` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `gare_UNIQUE`(`id` ASC))
ENGINE = InnoDB;

-- table Mine
CREATE TABLE IF NOT EXISTS `Mine` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` INT NOT NULL,
  `qteRessources` INT NOT NULL DEFAULT 100,
  `max` INT NOT NULL DEFAULT 10,
  `regen` INT NOT NULL DEFAULT 1000,
  `emplacement` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `mine_UNIQUE` (`id` ASC),
  CONSTRAINT `fk_gare_mine`
	FOREIGN KEY (`emplacement`)
    REFERENCES `Gare` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- table Train
-- La train n'a qu'un seul proprietaire et le joueur n'a qu'un seul train
CREATE TABLE IF NOT EXISTS `Train` (
  `proprietaire` VARCHAR(45) NOT NULL,
  `nom` VARCHAR(45), 
  `gareActuelle` INT NOT NULL, 
  PRIMARY KEY (`proprietaire`),
  UNIQUE INDEX `proprietaire_UNIQUE` (`proprietaire` ASC),
  CONSTRAINT `fk_joueur_train`
    FOREIGN KEY (`proprietaire`)
    REFERENCES `Joueur` (`nomJoueur`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_gare_train`
    FOREIGN KEY (`gareActuelle`)
    REFERENCES `Gare` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- table Wagon
CREATE TABLE IF NOT EXISTS `Wagon` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `proprietaire` VARCHAR(45) NOT NULL,
  `poids` INT NOT NULL DEFAULT 0,
  `niveau` INT NOT NULL DEFAULT 0,
  `typeID` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  CONSTRAINT `fk_train_wagon`
    FOREIGN KEY (`proprietaire`)
    REFERENCES `train` (`proprietaire`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- table offre
CREATE TABLE IF NOT EXISTS `Offres` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `trader` VARCHAR(45) NOT NULL,
  `offerType` INT NOT NULL DEFAULT 0,
  `offerAmount` INT NOT NULL DEFAULT 0,
  `priceType` INT NOT NULL,
  `priceAmount` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  CONSTRAINT `fk_joueur_offre`
    FOREIGN KEY (`trader`)
    REFERENCES `Joueur` (`nomJoueur`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET FOREIGN_KEY_CHECKS = 1;


