#!/bin/bash

# -------------------------------------------------------
# Programme de test en boÃ®te noire pour les 13 allumettes
# -------------------------------------------------------

usage() {
	echo "Erreur : $1"
	echo
	echo "Usage : sh testeur.sh [-d dossier] fichier.run..."
	exit 1
}

warning() {
	echo "**** $1" 1>&2
}

mainClass=allumettes.Jouer
mainFile=`echo $mainClass | tr . /`.java

# DÃ©terminer le dossier des sources
if [ -f $mainFile ] ; then
    src=.
    bin=.
elif [ -f src/$mainFile ] ; then
    src=src
    bin=bin
    [ -d "$bin" ] || mkdir "$bin"
else
    usage "La classe $mainClass est ni dans ./ ni dans src/"
fi

# Positionner CLASSPATH
export CLASSPATH=$src:$bin:${CLASSPATH:-.}

# DÃ©terminer si l'option -enconding latin1 est nÃ©cessaire
javacOpt=
if file -i $src/$mainFile |  grep iso-8859 > /dev/null 2>&1 ; then
    echo "Le fichier $src/$mainFile est en latin1."
    echo "   ==> Utilisation de l'option '-encoding latin1' de javac"
    javacOpt="-encoding latin1"
fi

# Traiter les arguments de la ligne de commande
# | Un seul argument possible -d pour dÃ©terminer le dossier dans lequel
# | mettre les rÃ©sutlats du test (.computed et .diff)
if [ "$1" = "-d" ] ; then
	shift
	testdiropt="$1"
	shift
	[ -d "$testdiropt" ] || mkdir "$testdiropt"
	if [ ! -d "$testdiropt" ] ; then
		usage "$testdiropt n'est pas un dossier"
	elif [ ! -w "$testdiropt" ] ; then
		usage "impossible d'Ã©crire dans $testdiropt"
	fi
fi

if [ ! -z "$testdiropt" ] ; then
	echo "Les rÃ©sultats seront dans $testdiropt."
fi


# Jouer les tests
if javac $javacOpt -d $bin $src/$mainFile ; then
    while [ "$1" ] ; do
	test="$1"
	shift

	if [ ! -f "$test" ] ; then
		warning "Fichier de test inexitant : $test"
		continue
	elif [ ! -r "$test" ] ; then
		warning "Fichier de test interdit en lecture : $test"
		continue
	fi

	testName=$(basename "$test" .run)
	testBasename=$(basename "$test")
	outputDir=${testdiropt:-$(dirname "$test")}

	if [ "$testName" = "$testBasename" ] ; then
		warning "Test ignorÃ© (le suffixe doit Ãªtre .run) : $test"
		continue
	fi


	# DÃ©finir les noms de fichiers utilisÃ©s
	computed=$outputDir/$testName.computed
	expected=${test%.run}.expected
	diff=$outputDir/$testName.diff


	if [ ! -r "$expected" ] ; then
		warning "Fichier de rÃ©sultat absent ou interdit en lecture : $expected"
		continue
	fi

	# Lancer le test
	sh $test > $computed 2>&1

	# Transformer le rÃ©sultat en utf8 (si nÃ©cessaire)
	if file -i $computed | grep iso-8859 > /dev/null 2>&1 ; then
	    echo "RÃ©sultat en latin1.  Je transforme en utf8."
	    recode latin1..utf8 $computed
	fi

	# Afficher le rÃ©sultat
	echo -n "$testName : "
	if diff -Bbw $computed $expected > $diff 2>&1 ; then
	    echo "ok"
	else
	    echo "ERREUR"
	    cat $diff
	    echo ""
	fi
    done
else
    echo "Erreur de compilation !"
fi