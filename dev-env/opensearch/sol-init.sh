#!/bin/bash
#
echo "init"
OUTPUT=$(/usr/share/opensearch/bin/opensearch-plugin list)
NORI="analysis-nori"

if [[ "$OUTPUT" == *"$NORI"* ]]; then
  echo "$NORI is already installed"
else
  echo " install $NORI"
  /usr/share/opensearch/bin/opensearch-plugin install $NORI
fi
