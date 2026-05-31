package com.example.data

object SeedData {
    fun getSeedTasks(): List<TaskEntity> {
        val criticalTasks = setOf(
            "Confirm section name: Hero / Arrival",
            "Confirm section name: Still Becoming",
            "Confirm section name: Current Mission",
            "Confirm section name: How I Learn",
            "Confirm section name: What’s Taking Shape",
            "Finalize life stages and lessons",
            "Finalize proof board cards",
            "Current Hemanth portrait",
            "Glasses transparent PNG/WebP",
            "CD/disc visual template",
            "Build static Hero section",
            "Build static Still Becoming section"
        )

        val importantTasks = setOf(
            "HeroMagnifierPrototype.tsx",
            "CDTimelinePrototype.tsx",
            "PersonaRevealPrototype.tsx",
            "MissionSplitPrototype.tsx",
            "ProofGridAssemblyPrototype.tsx",
            "Create V2 branch",
            "Backup V1",
            "GitHub Pages deployment"
        )

        val laterTasks = setOf(
            "Performance check",
            "Image optimization"
        )

        fun getPriority(taskText: String): String {
            return when {
                criticalTasks.any { taskText.contains(it) } -> "critical"
                importantTasks.any { taskText.contains(it) } -> "important"
                laterTasks.any { taskText.contains(it) } -> "later"
                else -> "later"
            }
        }

        val list = mutableListOf<TaskEntity>()

        // Helper to add tasks
        fun addTasks(
            phaseIdx: Int,
            phaseName: String,
            phaseNote: String,
            section: String,
            tasks: List<String>
        ) {
            tasks.forEachIndexed { taskIdx, text ->
                val id = "ph_${phaseIdx}_sec_${section.replace("/", "_").replace(" ", "_")}_task_${taskIdx}"
                list.add(
                    TaskEntity(
                        id = id,
                        phaseIndex = phaseIdx,
                        phaseName = phaseName,
                        phaseNote = phaseNote,
                        sectionLabel = section,
                        taskText = text,
                        isCompleted = false,
                        priority = getPriority(text)
                    )
                )
            }
        }

        // Phase 1
        addTasks(
            1, "Phase 1: Section requirements", "Lock the master template for each of the first five sections.",
            "Hero / Arrival",
            listOf(
                "Confirm section name: Hero / Arrival",
                "Confirm question: Who is Hemanth right now?",
                "Confirm design reference: Magnifying Hero Text Animation",
                "Confirm metaphor: Learning to see clearly",
                "Confirm hero copy and moving word list",
                "Confirm hero assets: portrait, glasses, lens mask, optional hand",
                "Define Hero initial / scroll / final animation states",
                "Define Hero desktop and mobile interaction",
                "Define Hero success criteria"
            )
        )
        addTasks(
            1, "Phase 1: Section requirements", "Lock the master template for each of the first five sections.",
            "Still Becoming",
            listOf(
                "Confirm section name: Still Becoming",
                "Confirm question: How was Hemanth shaped?",
                "Confirm design references: CD selector + Hover Mask Reveal",
                "Confirm metaphor: Identity archaeology",
                "Finalize life stages and lessons",
                "List persona assets needed for each stage",
                "Define Still Becoming initial / scroll / final animation states",
                "Define desktop CD interaction and mobile chapter adaptation",
                "Define Still Becoming success criteria"
            )
        )
        addTasks(
            1, "Phase 1: Section requirements", "Lock the master template for each of the first five sections.",
            "Current Mission",
            listOf(
                "Confirm section name: Current Mission",
                "Confirm question: What is Hemanth pursuing right now?",
                "Confirm design reference: 3D split scroll",
                "Confirm metaphor: One person → three missions",
                "Finalize missions: Proof Hub, AI Workflow Lab, UX Field Notes",
                "List mission assets and placeholder strategy",
                "Define Mission initial / scroll / final animation states",
                "Define desktop split/flip and mobile tap/stack adaptation",
                "Define Current Mission success criteria"
            )
        )
        addTasks(
            1, "Phase 1: Section requirements", "Lock the master template for each of the first five sections.",
            "How I Learn",
            listOf(
                "Confirm section name: How I Learn",
                "Confirm question: How does Hemanth turn curiosity into capability?",
                "Confirm design reference: minimal process loop",
                "Confirm metaphor: curiosity becomes capability through loops",
                "Finalize learning loop steps",
                "List step card/icon/connector assets",
                "Define Learning initial / scroll / final animation states",
                "Define desktop hover and mobile vertical stack",
                "Define How I Learn success criteria"
            )
        )
        addTasks(
            1, "Phase 1: Section requirements", "Lock the master template for each of the first five sections.",
            "What’s Taking Shape",
            listOf(
                "Confirm section name: What’s Taking Shape",
                "Confirm question: What am I actively turning into proof?",
                "Confirm design reference: eBay sticky image grid scroll",
                "Confirm metaphor: fragments becoming structure",
                "Finalize proof board cards",
                "List fragment/card assets needed",
                "Define Shape initial / scroll / final animation states",
                "Define desktop assembly and mobile stacked reveal",
                "Define What’s Taking Shape success criteria"
            )
        )

        // Phase 2
        addTasks(
            2, "Phase 2: Narrative and content", "Turn the requirements into usable website copy.",
            "Hero content",
            listOf(
                "Finalize micro-label: AI / Software / Design / Writing / Discipline",
                "Finalize headline: Building proof, not noise.",
                "Finalize subheadline",
                "Finalize live badge",
                "Finalize CTA labels"
            )
        )
        addTasks(
            2, "Phase 2: Narrative and content", "Turn the requirements into usable website copy.",
            "Still Becoming content",
            listOf(
                "Finalize Karate description and lesson",
                "Finalize Sainik School description and lesson",
                "Finalize Football + NCC description and lesson",
                "Finalize Poetry + Literature description and lesson",
                "Finalize Oratory description and lesson",
                "Finalize Entrepreneurship description and lesson",
                "Finalize Financial Literature description and lesson",
                "Finalize Fitness + Music description and lesson",
                "Finalize Building in Public description and lesson"
            )
        )
        addTasks(
            2, "Phase 2: Narrative and content", "Turn the requirements into usable website copy.",
            "Mission content",
            listOf(
                "Write Proof Hub front/back card copy",
                "Write AI Workflow Lab front/back card copy",
                "Write UX Field Notes front/back card copy",
                "Define status labels and next steps"
            )
        )
        addTasks(
            2, "Phase 2: Narrative and content", "Turn the requirements into usable website copy.",
            "Learning content",
            listOf(
                "Write Project step",
                "Write Problem step",
                "Write Skill step",
                "Write Artifact step",
                "Write Feedback step",
                "Write Iteration step"
            )
        )
        addTasks(
            2, "Phase 2: Narrative and content", "Turn the requirements into usable website copy.",
            "Shape content",
            listOf(
                "Write Proof Hub proof card",
                "Write AI Workflow Lab proof card",
                "Write UX Field Notes proof card",
                "Write First Real Product proof card",
                "Write honest closing line"
            )
        )

        // Phase 3
        addTasks(
            3, "Phase 3: Design system", "Define the rules that keep all sections connected.",
            "Visual language",
            listOf(
                "Choose font pair",
                "Define heading scale",
                "Define body text scale",
                "Lock dark cinematic palette",
                "Lock liquid-glass card style",
                "Define grain/noise overlay",
                "Define border/shadow system"
            )
        )
        addTasks(
            3, "Phase 3: Design system", "Define the rules that keep all sections connected.",
            "Motion rules",
            listOf(
                "Define slow cinematic motion timing",
                "Define medium interaction timing",
                "Define micro-interaction timing",
                "Choose easing curve",
                "Define reduced-motion fallback",
                "Define when not to animate"
            )
        )
        addTasks(
            3, "Phase 3: Design system", "Define the rules that keep all sections connected.",
            "Responsive rules",
            listOf(
                "Define desktop section height strategy",
                "Define tablet simplification rules",
                "Define mobile-first readability rules",
                "Define touch/tap alternatives for hover effects"
            )
        )

        // Phase 4
        addTasks(
            4, "Phase 4: Assets", "Gather or create the layers before coding motion.",
            "Hero assets",
            listOf(
                "Current Hemanth portrait",
                "Glasses transparent PNG/WebP",
                "Lens mask SVG or CSS shape",
                "Optional hand/arm layer",
                "Background texture",
                "Hero moving word list"
            )
        )
        addTasks(
            4, "Phase 4: Assets", "Gather or create the layers before coding motion.",
            "Still Becoming assets",
            listOf(
                "CD/disc visual template",
                "Current portrait base layer",
                "Karate persona visual",
                "Cadet/Sainik persona visual",
                "Athlete persona visual",
                "Poet/literature visual",
                "Orator/stage visual",
                "Entrepreneur visual",
                "Builder persona visual"
            )
        )
        addTasks(
            4, "Phase 4: Assets", "Gather or create the layers before coding motion.",
            "Mission assets",
            listOf(
                "Unified intro frame",
                "Proof Hub visual",
                "AI Workflow visual",
                "UX Field Notes visual",
                "Mission card front designs",
                "Mission card back designs"
            )
        )
        addTasks(
            4, "Phase 4: Assets", "Gather or create the layers before coding motion.",
            "Learning assets",
            listOf(
                "Project icon/card",
                "Problem icon/card",
                "Skill icon/card",
                "Artifact icon/card",
                "Feedback icon/card",
                "Iteration icon/card",
                "SVG connector line style"
            )
        )
        addTasks(
            4, "Phase 4: Assets", "Gather or create the layers before coding motion.",
            "Shape assets",
            listOf(
                "Proof Hub fragments",
                "AI prompt/workflow fragments",
                "UX observation fragments",
                "Problem-search fragments",
                "Status label designs",
                "Final proof board card design"
            )
        )

        // Phase 5
        addTasks(
            5, "Phase 5: Static design", "Design each scene in 3 states before motion.",
            "3-state sketches",
            listOf(
                "Sketch Hero initial / middle / final",
                "Sketch Still Becoming initial / active stage / final",
                "Sketch Current Mission unified / split / flipped final",
                "Sketch How I Learn intro / active steps / final loop",
                "Sketch What’s Taking Shape scattered / assembling / final board"
            )
        )
        addTasks(
            5, "Phase 5: Static design", "Design each scene in 3 states before motion.",
            "Static implementation",
            listOf(
                "Build static Hero section",
                "Build static Still Becoming section",
                "Build static Current Mission section",
                "Build static How I Learn section",
                "Build static What’s Taking Shape section",
                "Test static story flow before adding motion"
            )
        )

        // Phase 6
        addTasks(
            6, "Phase 6: Motion prototypes", "Build each effect separately before integrating.",
            "Prototype components",
            listOf(
                "HeroMagnifierPrototype.tsx",
                "CDTimelinePrototype.tsx",
                "PersonaRevealPrototype.tsx",
                "MissionSplitPrototype.tsx",
                "LearningLoopPrototype.tsx",
                "ProofGridAssemblyPrototype.tsx"
            )
        )
        addTasks(
            6, "Phase 6: Motion prototypes", "Build each effect separately before integrating.",
            "Prototype QA",
            listOf(
                "Check desktop performance",
                "Check mobile fallback",
                "Check reduced-motion fallback",
                "Confirm animation explains story",
                "Remove any animation that feels decorative only"
            )
        )

        // Phase 7
        addTasks(
            7, "Phase 7: Production build", "Move approved prototypes into the real Vite/React site.",
            "Repo setup",
            listOf(
                "Create V2 branch",
                "Backup V1",
                "Create src/sections structure",
                "Create src/components structure",
                "Create src/data structure",
                "Create public/assets/v2 folder"
            )
        )
        addTasks(
            7, "Phase 7: Production build", "Move approved prototypes into the real Vite/React site.",
            "Section integration",
            listOf(
                "Integrate HeroV2",
                "Integrate StillBecoming",
                "Integrate CurrentMission",
                "Integrate HowILearn",
                "Integrate WhatsTakingShape"
            )
        )
        addTasks(
            7, "Phase 7: Production build", "Move approved prototypes into the real Vite/React site.",
            "Testing and launch",
            listOf(
                "Desktop QA",
                "Tablet QA",
                "Mobile QA",
                "Performance check",
                "Image optimization",
                "GitHub Pages deployment",
                "Final live review"
            )
        )

        return list
    }
}
